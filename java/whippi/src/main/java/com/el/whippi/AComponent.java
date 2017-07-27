/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author david
 */
@AllArgsConstructor
@Getter
public abstract class AComponent {
    
    private final String tagName;
    
    protected abstract void render(WNode node, RenderResult result);
    
    protected final void renderChildren(WNode parent, RenderResult result) {
        for (WNode node : parent.getChildren()) {
            renderNode(node, result);
        }
    }
    
    protected final void renderNode(WNode node, RenderResult result) {
        WhippiParser.renderNode(node, result);
    }
    
    protected final void appendAllAttributes(WNode node, RenderResult result) {
        this.appendCid(node.getCid(), result);
        this.appendType(result);
        
        for (Map.Entry<String, Object> attr : node.getAttributes().entrySet()) {
            String name = attr.getKey();
            String value = attr.getValue().toString();
            if (name.equalsIgnoreCase("class")) {
                value = this.tagName + " " + value;
            }
            result.appendAttribute(name, value);
        }
    }
    
    protected final void appendType(RenderResult result) {
        result.appendAttribute("data-type", tagName);
    }

    protected final void appendCid(String cid, RenderResult result) {
        result.appendAttribute("data-cid", cid);
    }
    
}
