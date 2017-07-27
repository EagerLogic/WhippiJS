/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author david
 */
@AllArgsConstructor
@Getter
public abstract class ADirective {
    
    private final String tagName;
    
    protected abstract List<WNode> process(Element element, WNode node, ResolveContext ctx, RenderResult result);
    
    protected final List<WNode> resolveChildren(Element parentElement, WNode node, ResolveContext ctx, RenderResult result) {
        List<WNode> res = new ArrayList<>();
        for (int i = 0; i < parentElement.getChildNodes().getLength(); i++) {
            Node childNode = parentElement.getChildNodes().item(i);
            res.addAll(resolveChild(childNode, node, ctx, result));
        }
        return res;
    }
    
    protected final List<WNode> resolveChild(Node node, WNode parent, ResolveContext ctx, RenderResult result) {
        return WhippiParser.resolveNode(node, parent, ctx, result);
    }
    
}
