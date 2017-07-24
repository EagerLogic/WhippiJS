"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var directives;
var components;
var nextCid = 1;
var validExpressionStartChars = "$@#";
function registerDirective(directive) {
    directives[directive.tagName] = directive;
}
exports.registerDirective = registerDirective;
function getDirective(tagName) {
    return directives[tagName];
}
exports.getDirective = getDirective;
function registerComponent(component) {
    components[component.tagName] = component;
}
exports.registerComponent = registerComponent;
function getComponent(tagName) {
    return components[tagName];
}
exports.getComponent = getComponent;
// TODO register suit
function handleGet(url, req, resp) {
    throw "Not implemented yet!";
}
function handlePost(url, req, resp) {
    throw "Not implemented yet!";
}
function resolveDomNode(node, ctx) {
    if (node.nodeType === Node.CDATA_SECTION_NODE) {
        var res = new WNode();
        res.parent = ctx;
        res.type = "cdata";
        res.value = resolveToString(node.nodeValue, ctx.getModel());
        return [res];
    }
    else if (node.nodeType === Node.COMMENT_NODE) {
        var res = new WNode();
        res.parent = ctx;
        res.type = "comment";
        res.value = resolveToString(node.nodeValue, ctx);
        return [res];
    }
    else if (node.nodeType === Node.TEXT_NODE) {
        var res = new WNode();
        res.parent = ctx;
        res.type = "text";
        res.value = resolveToString(node.nodeValue, ctx);
        return [res];
    }
    else if (node.nodeType === Node.DOCUMENT_NODE || node.nodeType === Node.ELEMENT_NODE) {
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
        for (var i = 0; i < node.attributes.length; i++) {
            var attr = new Attribute();
            attr.name = node.attributes.item(i).nodeName;
            attr.value = resolveString(node.attributes.item(i).nodeValue, ctx);
            res.attributes.push(attr);
        }
        for (var i = 0; i < node.childNodes.length; i++) {
            res.children.concat(resolveDomNode(node.childNodes.item(i), res));
        }
        return [res];
    }
    else {
        throw "Unhandled xml element type: " + node.nodeType;
    }
}
exports.resolveDomNode = resolveDomNode;
function resolveModelNS(node, ctx) {
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
    }
    else if (modelExp.indexOf("${model[") == 0) {
        modelExp = unpackExpression(modelExp);
        modelExp = modelExp.substring(5);
        return ctx.modelNS + modelExp;
    }
    return null;
}
function resolveToString(input, ctx) {
    throw "Not implemented yet!";
}
exports.resolveToString = resolveToString;
function resolveString(input, ctx) {
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
exports.resolveString = resolveString;
function resolveExpression(input, ctx) {
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
exports.resolveExpression = resolveExpression;
function resolveReferenceExpression(exp, ctx) {
}
function resolveControllerReference(exp, ctx) {
}
function resolveCodeExpression(exp, ctx) {
}
function getNextCid() {
    return "cid-" + nextCid++;
}
exports.getNextCid = getNextCid;
function unpackExpression(exp) {
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
var ADirective = (function () {
    function ADirective(_tagName) {
        this._tagName = _tagName;
    }
    Object.defineProperty(ADirective.prototype, "tagName", {
        get: function () {
            return this._tagName;
        },
        enumerable: true,
        configurable: true
    });
    return ADirective;
}());
exports.ADirective = ADirective;
var AComponent = (function () {
    function AComponent(_tagName) {
        this._tagName = _tagName;
    }
    Object.defineProperty(AComponent.prototype, "tagName", {
        get: function () {
            return this._tagName;
        },
        enumerable: true,
        configurable: true
    });
    return AComponent;
}());
exports.AComponent = AComponent;
var WNode = (function () {
    function WNode() {
        this.parent = null;
        this.modelNS = null;
        this.attributes = [];
        this.children = [];
    }
    WNode.prototype.getModel = function () {
        return this.attributes["model"];
    };
    WNode.prototype.getAttributeRecursive = function (name) {
        var res = this.attributes[name];
        if (res == null && this.parent != null) {
            return this.parent.getAttributeRecursive(name);
        }
        return null;
    };
    return WNode;
}());
exports.WNode = WNode;
var Attribute = (function () {
    function Attribute() {
    }
    return Attribute;
}());
exports.Attribute = Attribute;
//# sourceMappingURL=parser.js.map