/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi.defaultsuit;

import com.el.whippi.AComponent;
import com.el.whippi.RenderResult;
import com.el.whippi.WNode;

/**
 *
 * @author david
 */
public class WButton extends AComponent {
    
    public WButton() {
        super("Button");
    }

    @Override
    protected void render(WNode node, RenderResult result) {
        result.appendBody("<button");
        this.appendAllAttributes(node, result);
        result.appendBody(">");
        
        this.renderChildren(node, result);
        
        result.appendBody("</button>");
    }
    
}
