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
public class WAnchorPanel extends AComponent {

    public WAnchorPanel() {
        super("AnchorPanel");
    }

    @Override
    protected void render(WNode node, RenderResult result) {
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
                .appendWidthStyle(node.getAttribute("width"))
                .appendHeightStyle(node.getAttribute("height"))
                .append(" ")
                .appendIfNotNull(node.getAttribute("style"))
                .append("\"");
        
        b.append(">\n");
        
        this.renderRow(node, result);
        
        b.append("</div>\n");
    }
    
    private void renderRow(WNode node, RenderResult result) {
        HtmlBuilder b = result.getBodyBuilder();
        
        b.append("<div")
                .appendAttribute("class", this.getTagName() + "-row");
        
        
        
        b.append(" style=\"")
                .appendStyle("display", "table-row")
                .append(" ")
                .appendIfNotNull(node.getAttribute("style-row"))
                .append("\"");
        
        b.append(">\n");
        
        this.renderCell(node, result);
        
        b.append("</div>\n");
    }
    
    private void renderCell(WNode node, RenderResult result) {
        HtmlBuilder b = result.getBodyBuilder();
        
        b.append("<div")
                .appendAttribute("class", this.getTagName() + "-cell");
        
        
        
        b.append(" style=\"")
                .appendStyle("display", "table-cell")
                .appendHAlignStyle(node.getAttribute("halign"))
                .appendVAlignStyle(node.getAttribute("valign"))
                .append(" ")
                .appendIfNotNull(node.getAttribute("style-cell"))
                .append("\"");
        
        b.append(">\n");
        
        this.renderChildren(node, result);
        
        b.append("</div>\n");
    }
    
}
