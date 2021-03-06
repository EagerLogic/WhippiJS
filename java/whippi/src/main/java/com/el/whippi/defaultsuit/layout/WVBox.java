/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi.defaultsuit.layout;

import com.el.whippi.AComponent;
import com.el.whippi.HtmlBuilder;
import com.el.whippi.RenderResult;
import com.el.whippi.WNode;

/**
 *
 * @author david
 */
public class WVBox extends AComponent {

    public WVBox() {
        super("VBox");
    }

    @Override
    protected void render(WNode node, RenderResult result) {
        if (node.getChildren().size() < 1) {
            return;
        }
        
        this.renderTable(node, result);
    }
    
    private void renderTable(WNode node, RenderResult result) {
        HtmlBuilder b = result.getBodyBuilder();
        
        b.append("<div")
                .appendAttribute("id", node.getAttribute("id"));
        this.appendCid(node.getCid(), result);
        this.appendType(result);
        this.appendClass(node.getAttribute("class"), result);
        
        
        
        b.append(" style=\"")
                .appendStyle("display", "inline-table")
                .appendHeightStyle(node.getAttribute("heigh"))
                .appendWidthStyle(node.getAttribute("width"))
                .append(" ")
                .appendIfNotNull(node.getAttribute("style"))
                .append("\"");
        
        b.append(">\n");
        
        for (WNode child : node.getChildren()) {
            if ("row".equals(child.getTagName())) {
                this.renderRow(node, child, result);
            }
        }
        
        b.append("</div>\n");
    }
    
    private void renderRow(WNode node, WNode child, RenderResult result) {
        HtmlBuilder b = result.getBodyBuilder();
        
        b.append("<div")
                .appendAttribute("class", this.getTagName() + "-row");
        
        
        
        b.append(" style=\"")
                .appendStyle("display", "table-row")
                .append(" ")
                .appendIfNotNull(node.getAttribute("style-row"))
                .append("\"");
        
        b.append(">\n");
        
        this.renderCell(child, result);
        
        b.append("</div>\n");
    }
    
    private void renderCell(WNode child, RenderResult result) {
        HtmlBuilder b = result.getBodyBuilder();
        
        b.append("<div")
                .appendAttribute("class", this.getTagName() + "-cell");
        
        
        
        b.append(" style=\"")
                .appendStyle("display", "table-cell")
                .appendHeightStyle(child.getAttribute("height"))
                .appendHAlignStyle(child.getAttribute("halign"))
                .appendVAlignStyle(child.getAttribute("valign"))
                .append(" ")
                .appendIfNotNull(child.getAttribute("style"))
                .append("\"");
        
        b.append(">\n");
        
        this.renderChildren(child, result);
        
        b.append("</div>\n");
    }
    
}
