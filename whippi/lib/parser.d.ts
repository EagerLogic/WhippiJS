export declare function registerDirective(directive: ADirective): void;
export declare function getDirective(tagName: string): ADirective;
export declare function registerComponent(component: AComponent): void;
export declare function getComponent(tagName: string): AComponent;
export declare function resolveDomNode(node: Node, ctx: WNode): WNode[];
export declare function resolveToString(input: string, ctx: WNode): string;
export declare function resolveString(input: string, ctx: WNode): string | {};
export declare function resolveExpression(input: string, ctx: WNode): string | {};
export declare function getNextCid(): string;
export declare abstract class ADirective {
    private _tagName;
    constructor(_tagName: string);
    readonly tagName: string;
    abstract parse(node: Node, parent: WNode): WNode[];
}
export declare abstract class AComponent {
    private _tagName;
    constructor(_tagName: string);
    readonly tagName: string;
}
export declare class WNode {
    tagName: string;
    value: string;
    cid: string;
    parent: WNode;
    modelNS: string;
    attributes: Attribute[];
    children: WNode[];
    type: "element" | "cdata" | "comment" | "text";
    getModel(): any;
    getAttributeRecursive(name: string): any;
}
export declare class Attribute {
    name: string;
    value: string | {};
}
