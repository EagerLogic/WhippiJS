import * as runtime from "./runtime";


class ForkDirective extends runtime.ADirective {

    constructor() {
        super(":fork");
    }

    public parse(node: Node, ctx: runtime.WNode): runtime.WNode[] {
        return null;
    }

}

export const suite: runtime.Suite = new runtime.Suite();
suite.addDirective(new ForkDirective());