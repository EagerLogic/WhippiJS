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
 * @author ipacsdavid
 */
public class WDialog extends AComponent {

    public WDialog() {
        super("Dialog");
    }

    @Override
    protected void render(WNode node, RenderResult result) {
        if (!"true".equalsIgnoreCase(node.getAttributeString("visible"))) {
            return;
        }
        
        result.appendHeadOnce("dialogPrevent", "<script>function preventDialogClose(e) {if(e.stopPropagation){e.stopPropagation();}else{event.cancelBubble = true;}}</script>");
        
        HtmlBuilder b = result.getBodyBuilder();
        
        b.append("<div")
                .appendAttribute("id", node.getAttribute("id"));
        this.appendCid(node.getCid(), result);
        this.appendType(result);
        this.appendClass(node.getAttribute("class"), result);
        
        if (node.getAttribute("onGlassClicked") != null) {
            b.appendAttribute("onclick", node.getAttribute("onGlassClicked"));
        }
        
        String glassColor = node.getAttributeString("glasscolor");
        if (glassColor == null) {
            glassColor = "rgba(0,0,0, 0.6)";
        }
        
        b.append(" style=\"")
                .appendStyle("position", "fixed")
                .appendStyle("display", "inline-block")
                .appendStyle("text-align", "center")
                .appendStyle("overflow-y", "auto")
                .appendStyle("padding", "80px")
                .appendStyle("z-index", "" + ((10 + result.getNextDialogNumber()) * 10000))
                .appendStyle("background-color", glassColor)
                .appendStyle("left", "0px")
                .appendStyle("top", "0px")
                .appendWidthStyle("100%")
                .appendHeightStyle("100%")
                .append(" ")
                .appendIfNotNull(node.getAttribute("style"))
                .append("\"");
        
        b.append(">\n");
        
        b.append("<div style=\"display: inline-block\" onclick=\"preventDialogClose(event)\">\n");
        
        this.renderChildren(node, result);
        
        b.append("</div>\n");
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
        
        if (node.getAttribute("onGlassClicked") != null) {
            b.appendAttribute("onclick", node.getAttribute("onGlassClicked"));
        }
        
        
        
        b.append(" style=\"")
                .appendStyle("display", "table-cell")
                .appendStyle("padding", "40px")
                .appendHAlignStyle("center")
                .appendVAlignStyle("middle")
                .append(" ")
                .appendIfNotNull(node.getAttribute("style-cell"))
                .append("\"");
        
        b.append(">\n");
        
        b.append("<div style=\"display: inline-block; overflow-y: auto;\">\n");
        
        this.renderChildren(node, result);
        
        b.append("</div>\n");
        b.append("</div>\n");
    }
    
}
