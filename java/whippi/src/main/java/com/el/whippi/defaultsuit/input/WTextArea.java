/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi.defaultsuit.input;

import com.el.whippi.AComponent;
import com.el.whippi.HtmlBuilder;
import com.el.whippi.RenderResult;
import com.el.whippi.WNode;

/**
 *
 * @author david
 */
public class WTextArea extends AComponent {

    public WTextArea() {
        super("TextArea");
    }

    @Override
    protected void render(WNode node, RenderResult result) {
        HtmlBuilder b = result.getBodyBuilder();
        
        b.append("<textarea");
        this.appendCid(node.getCid(), result);
        this.appendType(result);
        this.appendClass(node.getAttribute("class"), result);
        this.appendAllAttributes(node, result, "class", "style", "oninput", "model");
        
        renderOnInput(node, b);
        
        renderStyle(b, node);
        
        b.append(">\n");
        
        b.appendIfNotNull(HtmlBuilder.escapeHtml(node.getModel()));
        
        b.append("</textarea>\n");
        
    }

    private void renderOnInput(WNode node, HtmlBuilder b) {
        String onInput = "";
        if (node.getModelNS() != null) {
            onInput = node.getModelNS() + "=this.value;";
        }
        if (node.getAttribute("oninput") != null) {
            onInput += node.getAttribute("oninput");
        }
        b.appendAttribute("oninput", onInput);
    }

    private void renderStyle(HtmlBuilder b, WNode node) {
        b.append(" style=\"");
        b.appendWidthStyle(node.getAttribute("width"));
        b.appendHeightStyle(node.getAttribute("height"));
        b.append("\"");
    }

    
    
}
