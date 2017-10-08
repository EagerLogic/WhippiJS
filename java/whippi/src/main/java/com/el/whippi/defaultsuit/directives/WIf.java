/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi.defaultsuit.directives;

import com.el.whippi.ADirective;
import com.el.whippi.RenderResult;
import com.el.whippi.ResolveContext;
import com.el.whippi.WNode;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;

/**
 *
 * @author ipacsdavid
 */
public class WIf extends ADirective {

    public WIf() {
        super(":if");
    }

    @Override
    protected List<WNode> process(Element element, WNode node, ResolveContext ctx, RenderResult result) {
        Object condition = node.getAttribute("condition");
        if (condition == null) {
            return new ArrayList<>();
        }
        if (!Boolean.FALSE.equals(condition)) {
            return this.resolveChildren(element, node, ctx, result);
        }
        return new ArrayList<>();
    }

}
