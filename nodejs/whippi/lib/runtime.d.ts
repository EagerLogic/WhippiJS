export declare function registerDirective(directive: ADirective): void;
export declare function getDirective(tagName: string): ADirective;
export declare function registerComponent(component: AComponent): void;
export declare function getComponent(tagName: string): AComponent;
export declare function registerSuit(suite: Suite): void;
export declare function resolveDomNode(node: Node, ctx: WNode): WNode[];
export declare function resolveToString(input: string, ctx: WNode): string;
export declare function resolveString(input: string, ctx: WNode): {};
export declare function resolveExpression(input: string, ctx: WNode): {};
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
    abstract render(node: WNode): any;
}
export declare class WNode {
    tagName: string;
    value: string;
    cid: string;
    parent: WNode;
    modelNS: string;
    attributes: Map<object>;
    children: WNode[];
    type: "element" | "cdata" | "comment" | "text";
    getModel(): any;
    getAttributeRecursive(name: string): any;
}
export declare class Suite {
    directives: ADirective[];
    components: AComponent[];
    addDirective(directive: ADirective): void;
    addComponent(component: AComponent): void;
}
export declare class RenderResult {
    private _head;
    private _body;
    private _headOnce;
    private _bodyOnce;
    private _modelRegister;
    private _logItems;
    private _hasError;
    readonly modelRegister: Map<string>;
    readonly head: string;
    readonly body: string;
    readonly headOnce: string[];
    readonly bodyOnce: string[];
    appendHead(head: string): void;
    appendHeadOnce(key: string, head: string): void;
    appendBody(body: string): void;
    appendBodyOnce(key: string, body: string): void;
    log(level: "info" | "warning" | "error", message: string, node: WNode): void;
    readonly hasError: boolean;
    printLog(): void;
}
export declare class Map<$Value> {
    private arr;
    get(key: string): $Value;
    getEntry(key: string): MapEntry<$Value>;
    put(key: string, value: $Value): $Value;
    remove(key: string): $Value;
    private indexOf(key);
    readonly length: number;
    readonly entries: MapEntry<$Value>[];
}
export declare class MapEntry<$Value> {
    private _key;
    private _value;
    constructor(_key: string, _value: $Value);
    readonly key: string;
    readonly value: $Value;
}
