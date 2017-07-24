import * as xmldom from 'xmldom';
import * as http from 'http';



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

// TODO register suit

function handleGet(url: string, req: http.IncomingMessage, resp: http.ServerResponse): string {
    throw "Not implemented yet!";
}

function handlePost(url: string, req: http.IncomingMessage, resp: http.ServerResponse): string {
    throw "Not implemented yet!";
}

export function resolveDomNode(node: Node, ctx: WNode): WNode[] {
    if (node.nodeType === Node.CDATA_SECTION_NODE) {
        var res = new WNode();
        res.parent = ctx;
        res.type = "cdata";
        res.value = resolveToString(node.nodeValue, ctx.getModel());
        return [res];
    } else if (node.nodeType === Node.COMMENT_NODE) {
        var res = new WNode();
        res.parent = ctx;
        res.type = "comment";
        res.value = resolveToString(node.nodeValue, ctx);
        return [res];
    } else if (node.nodeType === Node.TEXT_NODE) {
        var res = new WNode();
        res.parent = ctx;
        res.type = "text";
        res.value = resolveToString(node.nodeValue, ctx);
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
            let attr = new Attribute();
            attr.name = node.attributes.item(i).nodeName;
            attr.value = resolveString(node.attributes.item(i).nodeValue, ctx);
            res.attributes.push(attr);
        }
        for (let i = 0; i < node.childNodes.length; i++) {
            res.children.concat(resolveDomNode(node.childNodes.item(i), res));
        }
        return [res];
    } else {
        throw "Unhandled xml element type: " + node.nodeType;
    }
}

function parsePageTree(root: WNode) {
    
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

export function resolveString(input: string, ctx: WNode): string | {} {
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

export function resolveExpression(input: string, ctx: WNode): string | {} {
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

function resolveReferenceExpression(exp: string, ctx: WNode): string | {} {
    exp = resolveToString(exp, ctx);
    return eval(exp);
}

function resolveControllerReference(exp:string, ctx: WNode): string {
    exp = resolveToString(exp, ctx);
    return "wpi.callAction('" + exp + "')";
}

function resolveCodeExpression(exp: string, ctx: WNode): string | {} {
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

}

export class WNode {
    public tagName: string;
    public value: string;
    public cid: string;
    public parent: WNode = null;
    public modelNS: string = null;
    public attributes: Attribute[] = [];
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

export class Attribute {
    public name: string;
    public value: string | {};
}

