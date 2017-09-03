/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi.defaultsuit;

import com.el.whippi.AComponent;
import com.el.whippi.HtmlBuilder;
import com.el.whippi.RenderResult;
import com.el.whippi.WNode;

/**
 *
 * @author david
 */
public class WFontAwesome extends AComponent {

    public WFontAwesome() {
        super("FA");
    }

    @Override
    protected void render(WNode node, RenderResult result) {
        result.appendBodyOnce("FontAwesome", "<script src=\"https://use.fontawesome.com/a7d8d67d15.js\"></script>");
        
        String tagName = node.getAttributeString("tag");
        if (tagName == null) {
            tagName = "i";
        }
        
        String icon = node.getAttributeString("icon");
        if (icon == null) {
            icon = "question-circle";
        }
        
        String color = node.getAttributeString("color");
        String size = node.getAttributeString("size");
        boolean fixedWidth = "true".equalsIgnoreCase(node.getAttributeString("fixedWidth"));
        boolean spin = "true".equalsIgnoreCase(node.getAttributeString("spin"));
        
        String extraClass = node.getAttributeString("class");
        String extraStyle = node.getAttributeString("style");
        
        String computedClass = "fa fa-" + icon;
        if (fixedWidth) {
            computedClass += " fa-fw";
        }
        if (spin) {
            computedClass += " fa-spin";
        }
        if (extraClass != null) {
            computedClass += " " + extraClass;
        }
        
        String computedStyle = "";
        if (color != null) {
            computedStyle += "color:" + color + ";";
        }
        if (size != null) {
            computedStyle += "font-size:" + size + ";";
        }
        if (extraStyle != null) {
            computedStyle += extraStyle;
        }
        
        HtmlBuilder b = result.getBodyBuilder();
        b.append("<").append(tagName);
        this.appendClass(computedClass, result);
        b.appendAttribute("style", computedStyle);
        
        b.append("></").append(tagName).append(">");
       
        
    }
    
}
