import * as xmldom from 'xmldom';
import * as http from 'http';
import * as fs from "fs";
import * as URL from "url";



var directives: { [tagName: string]: ADirective };
var components: { [tagName: string]: AComponent };
var nextCid: number = 1;
var validExpressionStartChars = "$@#"

export function registerDirective(directive: ADirective) {
    directives[directive.tagName] = directive;
}

export function getDirective(tagName: string) {
    return directives[tagName];
}

export function registerComponent(component: AComponent) {
    components[component.tagName] = component;
}

export function getComponent(tagName: string) {
    return components[tagName];
}

export function registerSuit(suite: Suite) {
    for (var directive of suite.directives) {
        registerDirective(directive);
    }
    for (var component of suite.components) {
        registerComponent(component);
    }
}

function handleGet(url: string, req: http.IncomingMessage, resp: http.ServerResponse, wpiBaseUrl: string, wpicBaseUrl: string): void {
    var fileUrl = wpiBaseUrl + url;
    if (!fs.existsSync(fileUrl)) {
        resp.statusCode = 404;
        resp.statusMessage = "Can't find whippi page descriptor: " + fileUrl;
        resp.end();
        return;
    }

    var xml = fs.readFileSync(fileUrl, "UTF-8");
    var doc = new xmldom.DOMParser().parseFromString(xml);
    var rootDom: Node = doc.documentElement;
    var controllerAttr = rootDom.attributes.getNamedItem("controller");
    if (controllerAttr == null) {
        console.error("Missing controller attribute of page: " + url);
        resp.statusCode = 500;
        resp.statusMessage = "Missing controller attribute of page: " + url;
        resp.end();
        return;
    }
    var controllerStr = controllerAttr.nodeValue;
    var controller: AController<any>;
    try {
        controller = eval("new " + controllerStr + "();");
    } catch (e) {
        console.error("Can't create controller instance: " + controllerStr);
        resp.statusCode = 500;
        resp.statusMessage = "Can't create controller instance: " + controllerStr;
        resp.end();
        throw e;
    }

    if (controller == null) {
        console.error("Can't create controller instance: " + controllerStr);
        resp.statusCode = 500;
        resp.statusMessage = "Can't create controller instance: " + controllerStr;
        resp.end();
        return;
    }

    var query = URL.parse(req.url,true).query;
    var attributes = new Map<string>();
    for (var key in query) {
        var value = query[key];
        if (typeof value !== "function") {
            attributes.put(key, value);
        }
    }

    var model;
    try {
        model = controller.load(attributes);
    } catch (e) {
        if (e instanceof RedirectException) {
            sendRedirect((<RedirectException>e).redirectUrl, resp);
            return;
        }
        throw e;
    }

    var rootNodes: WNode[] = resolveDomNode(rootDom, null);
    if (rootNodes.length > 1) {
        console.error("Invalid xml, multiple root elements: " + url);
        resp.statusCode = 500;
        resp.statusMessage = "Invalid xml, multiple root elements: " + url;
        resp.end();
        return;
    }

    if (rootNodes.length < 1) {
        console.error("Invalid xml, no root elements: " + url);
        resp.statusCode = 500;
        resp.statusMessage = "Invalid xml, no root elements: " + url;
        resp.end();
        return;
    }

    var rootNode: WNode = rootNodes[0];
}

function handlePost(url: string, req: http.IncomingMessage, resp: http.ServerResponse): string {
    throw "Not implemented yet!";
}

function sendRedirect(url: string, resp: http.ServerResponse) {
    resp.statusCode = 302;
    resp.setHeader("Location", url);
    resp.end();
}

export function resolveDomNode(node: Node, ctx: WNode): WNode[] {
    if (node.nodeType === Node.CDATA_SECTION_NODE) {
        var res = new WNode();
        res.parent = ctx;
        res.type = "cdata";
        res.value = resolveToString(node.textContent, ctx.getModel());
        return [res];
    } else if (node.nodeType === Node.COMMENT_NODE) {
        var res = new WNode();
        res.parent = ctx;
        res.type = "comment";
        res.value = resolveToString(node.textContent, ctx);
        return [res];
    } else if (node.nodeType === Node.TEXT_NODE) {
        var res = new WNode();
        res.parent = ctx;
        res.type = "text";
        res.value = resolveToString(node.textContent, ctx);
        return [res];
    } else if (node.nodeType === Node.DOCUMENT_NODE || node.nodeType === Node.ELEMENT_NODE) {
        var tagName = node.nodeName;
        var directive = getDirective(tagName);
        if (directive != null) {
            return directive.parse(node, ctx);
        }

        var res = new WNode();
        res.cid = getNextCid();
        res.type = "element";
        res.tagName = tagName;
        res.parent = ctx;
        res.modelNS = resolveModelNS(node, ctx);
        for (let i = 0; i < node.attributes.length; i++) {
            res.attributes.put(node.attributes.item(i).nodeName, resolveString(node.attributes.item(i).nodeValue, ctx));
        }
        for (let i = 0; i < node.childNodes.length; i++) {
            res.children = res.children.concat(resolveDomNode(node.childNodes.item(i), res));
        }
        return [res];
    } else {
        throw "Unhandled xml element type: " + node.nodeType;
    }
}

function resolveModelNS(node: Node, ctx: WNode): string {
    if (ctx == null) {
        return null;
    }
    if (ctx.parent == null) {
        return "";
    }
    if (ctx.modelNS == null) {
        return null;
    }
    var modelAttr = node.attributes.getNamedItem("model");
    if (modelAttr == null) {
        return null;
    }
    var modelExp = modelAttr.nodeValue;
    if (modelExp === "${model}") {
        return ctx.modelNS;
    }
    if (modelExp.indexOf("${model.") == 0) {
        modelExp = unpackExpression(modelExp);
        modelExp = modelExp.substring(5);
        return ctx.modelNS + modelExp;
    } else if (modelExp.indexOf("${model[") == 0) {
        modelExp = unpackExpression(modelExp);
        modelExp = modelExp.substring(5);
        return ctx.modelNS + modelExp;
    }
    return null;
}

export function resolveToString(input: string, ctx: WNode): string {
    throw "Not implemented yet!";
}

export function resolveString(input: string, ctx: WNode): {} {
    if (input == null) {
        return null;
    }
    if (input.length < 3) {
        return input;
    }
    if (validExpressionStartChars.indexOf(input.charAt(0)) > -1 && input.charAt(1) === "{" && input.charAt(input.length - 1) === "}") {
        return resolveExpression(input, ctx);
    }
    return resolveToString(input, ctx);
}

export function resolveExpression(input: string, ctx: WNode): {} {
    var exp = unpackExpression(input);
    if (exp == null) {
        throw "Not a valid expression: '" + input + "'";
    }

    var firstChar = input.charAt(0);
    if (firstChar === "$") {
        return resolveReferenceExpression(exp, ctx);
    }

    if (firstChar === "@") {
        return resolveControllerReference(exp, ctx);
    }

    if (firstChar === "#") {
        return resolveCodeExpression(exp, ctx);
    }

    throw "Unhandled expression start char: '" + input.charAt(0) + "'";
}

function resolveReferenceExpression(exp: string, ctx: WNode): object {
    exp = resolveToString(exp, ctx);
    return eval(exp);
}

function resolveControllerReference(exp: string, ctx: WNode): string {
    exp = resolveToString(exp, ctx);
    return "wpi.callAction('" + exp + "')";
}

function resolveCodeExpression(exp: string, ctx: WNode): object {
    exp = resolveToString(exp, ctx);
    return eval(exp);
}

export function getNextCid() {
    return "cid-" + nextCid++;
}

function unpackExpression(exp: string): string {
    if (exp == null) {
        return null;
    }
    if (exp.length < 3) {
        return null;
    }

    var firstChar = exp.charAt(0);
    var secondChar = exp.charAt(1);
    var lastChar = exp.charAt(exp.length - 1);
    if (validExpressionStartChars.indexOf(firstChar) > -1 && secondChar == "{" && lastChar == "}") {
        return exp.slice(2, exp.length - 1);
    }
    return null;
}

function renderNode(node: WNode, result: RenderResult): void {
    throw "Not implemented yet!";
}

function renderPage(node: WNode, result: RenderResult): string {
    throw "Not implemented yet!";
}

function renderComposite(node: WNode, result: RenderResult): string {
    throw "Not implemented yet!";
}

function renderHtml(node: WNode, result: RenderResult): string {
    throw "Not implemented yet!";
}



export abstract class ADirective {

    constructor(private _tagName: string) { }

    public get tagName() {
        return this._tagName;
    }

    public abstract parse(node: Node, parent: WNode): WNode[];

}

export abstract class AComponent {

    constructor(private _tagName: string) {

    }

    get tagName() {
        return this._tagName;
    }

    public abstract render(node: WNode);

}

export class WNode {
    public tagName: string;
    public value: string;
    public cid: string;
    public parent: WNode = null;
    public modelNS: string = null;
    public attributes = new Map<object>();
    public children: WNode[] = [];
    public type: "element" | "cdata" | "comment" | "text";

    getModel() {
        return this.attributes["model"];
    }

    getAttributeRecursive(name: string) {
        var res = this.attributes[name];
        if (res == null && this.parent != null) {
            return this.parent.getAttributeRecursive(name);
        }
        return null;
    }

}

// export class Attribute {
//     public name: string;
//     public value: object;
// }

export class Suite {

    public directives: ADirective[] = [];
    public components: AComponent[] = [];

    public addDirective(directive: ADirective) {
        this.directives.push(directive);
    }

    public addComponent(component: AComponent) {
        this.components.push(component);
    }

}

export class RenderResult {

    private _head: string = "";
    private _body: string = "";
    private _headOnce = new Map<string>();
    private _bodyOnce = new Map<string>();
    private _modelRegister = new Map<string>();
    private _logItems: LogItem[] = [];
    private _hasError: boolean = false;

    get modelRegister() {
        return this._modelRegister;
    }
    get head() {
        return this._head;
    }
    get body() {
        return this._body;
    }
    get headOnce(): string[] {
        var res: string[] = [];
        for (var entry of this._headOnce.entries) {
            res.push(entry.value);
        }
        return res;
    }
    get bodyOnce(): string[] {
        var res: string[] = [];
        for (var entry of this._bodyOnce.entries) {
            res.push(entry.value);
        }
        return res;
    }

    public appendHead(head: string) {
        this._head = this._head + head;
    }

    public appendHeadOnce(key: string, head: string) {
        this._headOnce.put(key, head);
    }

    public appendBody(body: string) {
        this._body = this._body + body;
    }

    public appendBodyOnce(key: string, body: string) {
        this._bodyOnce.put(key, body);
    }

    public log(level: "info" | "warning" | "error", message: string, node: WNode) {
        this._logItems.push(new LogItem(level, message, node));
        if (!this._hasError) {
            if (level == "error") {
                this._hasError = true;
            }
        }
    }

    public get hasError() {
        return this._hasError;
    }

    public printLog() {
        for (var item of this._logItems) {
            if (item.level == "info") {
                console.info(item.message);
            } else if (item.level = "warning") {
                console.warn(item.message);
            } else {
                console.error(item.message);
            }
        }
    }
}

export class Map<$Value> {

    private arr: MapEntry<$Value>[] = [];

    public get(key: string): $Value {
        var entry = this.getEntry(key);
        if (entry == null) {
            return null;
        }
        return entry.value;
    }

    public getEntry(key: string): MapEntry<$Value> {
        var idx = this.indexOf(key);
        if (idx < 0) {
            return null;
        }
        return this.arr[idx];
    }

    public put(key: string, value: $Value): $Value {
        if (value == null) {
            // TODO remove
            return;
        }
        if (key == null) {
            throw "Null keys not allowed!"
        }
        var res = null;
        var idx = this.indexOf(key);
        if (idx > -1) {
            res = this.arr[idx].value;
            this.arr[idx] = new MapEntry<$Value>(key, value);
        } else {
            this.arr.push(new MapEntry<$Value>(key, value));
        }
        return res;
    }

    public remove(key: string): $Value {
        var res = null;
        var idx = this.indexOf(key);
        if (idx < 0) {
            return null;
        }

        res = this.arr[idx].value;
        this.arr.splice(idx, 1);
        return res;
    }

    private indexOf(key: string): number {
        for (var i = 0; i < this.arr.length; i++) {
            var entry = this.arr[i];
            if (entry.key === key) {
                return i;
            }
        }
        return -1;
    }

    public get length() {
        return this.arr.length;
    }

    public get entries(): MapEntry<$Value>[] {
        return [].concat(this.arr);
    }

}

export class MapEntry<$Value> {

    constructor(private _key: string, private _value: $Value) { }

    get key() {
        return this._key;
    }

    get value() {
        return this._value;
    }

}

export abstract class AController<$Model> {

    public abstract preLoad(attributes: Map<string>): void;
    public abstract load(attributes: Map<string>): $Model;

}

export class RedirectException {

    constructor(private _redirectUrl: string) {};

    get redirectUrl() {
        return this._redirectUrl;
    }

}

class LogItem {

    constructor(public level: "info" | "warning" | "error", public message: string, node: WNode) { }

}

