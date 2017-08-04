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
    
    protected final void appendAllAttributes(WNode node, RenderResult result, String... except) {
        this.appendCid(node.getCid(), result);
        this.appendType(result);
        
        String classes = this.tagName;
        for (Map.Entry<String, Object> attr : node.getAttributes().entrySet()) {
            String name = attr.getKey();
            if (except != null) {
                boolean skip = false;
                for (String ex : except) {
                    if (name.equalsIgnoreCase(ex)) {
                        skip = true;
                        break;
                    }
                }
                if (skip) {
                    continue;
                }
            }
            String value = attr.getValue().toString();
            if (name.equalsIgnoreCase("class")) {
                classes = classes + " " + value;
                continue;
            }
            result.appendAttribute(name, value);
        }
        result.appendAttribute("class", classes);
    }
    
    protected final void appendType(RenderResult result) {
        result.appendAttribute("data-ctype", tagName);
    }

    protected final void appendCid(String cid, RenderResult result) {
        result.appendAttribute("data-cid", cid);
    }
    
    protected final void appendClass(Object classes, RenderResult res) {
        res.getBodyBuilder()
                .append(" class=\"").append(this.tagName);
        
        if (classes != null) {
            res.getBodyBuilder().append(" ").append(classes);
        }
        
        res.getBodyBuilder().append("\"");
    }
    
}
