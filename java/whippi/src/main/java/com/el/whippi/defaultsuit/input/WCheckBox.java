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
public class WCheckBox extends AComponent {

    public WCheckBox() {
        super("CheckBox");
    }

    @Override
    protected void render(WNode node, RenderResult result) {
        HtmlBuilder b = result.getBodyBuilder();
        
        b.append("<input").appendAttribute("type", "checkbox");
        this.appendCid(node.getCid(), result);
        this.appendType(result);
        if (node.getModel() != null) {
            if (node.getModel() instanceof Boolean) {
                if ((Boolean)node.getModel() == true) {
                    b.appendAttribute("checked", "true");
                }
            } else {
                result.logError("Invalid CheckBox model! It must be boolean, but it's " + node.getModel().getClass().getName(), node);
            }
        }
        this.appendAllAttributes(node, result, "onchange", "type");
        String onchange = "";
        if (node.getModelNS() != null) {
            onchange = node.getModelNS() + "=this.checked;";
        }
        if (node.getAttribute("onchange") != null) {
            onchange += node.getAttribute("onchange");
        }
        if (onchange.trim().length() > 0) {
            b.appendAttribute("onchange", onchange);
        }
        
        b.append("/>\n");
    }
    
}
