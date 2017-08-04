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
public class WTextBox extends AComponent {

    public WTextBox() {
        super("TextBox");
    }

    @Override
    protected void render(WNode node, RenderResult result) {
        
        
        result.appendBody("<input type=\"" + (node.getAttribute("type") == null ? "text" : node.getAttribute("type")) + "\"");
        this.appendCid(node.getCid(), result);
        this.appendType(result);
        
        result.appendAttribute("value", "" + HtmlBuilder.escapeHtml(node.getModel()));
        this.appendAllAttributes(node, result, "oninput", "value", "type");
        
        
        String onInput = "";
        if (node.getModelNS() != null) {
            onInput = node.getModelNS() + "=this.value;";
        }
        if (node.getAttribute("oninput") != null) {
            onInput += node.getAttribute("oninput");
        }
        result.appendAttribute("oninput", onInput);
        
        result.appendBody("></input>");
    }
    
}
