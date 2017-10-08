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
import org.w3c.dom.Node;

/**
 *
 * @author david
 */
public class WSwitch extends ADirective {

    public WSwitch() {
        super(":switch");
    }

    @Override
    protected List<WNode> process(Element element, WNode node, ResolveContext ctx, RenderResult result) {
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            Node childNode = element.getChildNodes().item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            
            Element child = (Element) childNode;
            if (child.getTagName().equals(":case")) {
                WNode childWNode = this.resolveChild(child, node, ctx, result).get(0);
                Object condition = childWNode.getAttribute("condition");
                if (condition == null) {
                    continue;
                }
                if (!Boolean.FALSE.equals(condition)) {
                    return this.resolveChildren(child, node, ctx, result);
                }
            } else if (child.getTagName().equals(":default")) {
                return this.resolveChildren(child, node, ctx, result);
            } else {
                result.logWarning("Skipping tag: " + child.getTagName() + ". Valid child elements of :fork directive are: ':branch' and ':default'.", node);
            }
        }
        
        return new ArrayList<>();
    }
    
}
