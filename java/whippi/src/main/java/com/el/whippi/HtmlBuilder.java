/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

/**
 *
 * @author david
 */
public class HtmlBuilder {
    
    public static String escapeHtml(Object input) {
        if (input == null) {
            return null;
        }
        
        return input.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&qout;").replace("'", "&#39;");
    }
    
    private final StringBuilder sb;

    public HtmlBuilder() {
        this(new StringBuilder());
    }
    
    public HtmlBuilder(StringBuilder sb) {
        this.sb = sb;
    }
    
    public HtmlBuilder appendAttribute(String name, Object value) {
        if (value == null) {
            return this;
        }
        
        sb.append(" ").append(name).append("=\"").append(value.toString()).append("\"");
        return this;
    }

    public HtmlBuilder append(Object value) {
        sb.append(value);
        return this;
    }
    
    public HtmlBuilder appendIfNotNull(Object value) {
        if (value != null) {
            sb.append(value);
        }
        return this;
    }
    
    public HtmlBuilder appendWidthStyle(Object value) {
        return appendStyle("width", value);
    }
    
    public HtmlBuilder appendHeightStyle(Object value) {
        return appendStyle("height", value);
    }
    
    public HtmlBuilder appendHAlignStyle(Object value) {
        return appendStyle("text-align", value == null ? "left" : value);
    }
    
    public HtmlBuilder appendVAlignStyle(Object value) {
        return appendStyle("vertical-align", value == null ? "top" : value);
    }
    
    public HtmlBuilder appendPaddingStyle(Object value) {
        return appendStyle("padding", null, value);
    }
    
    public HtmlBuilder appendPaddingStyle(String side, Object value) {
        return appendStyle("padding", side, value);
    }
    
    public HtmlBuilder appendMarginStyle(Object value) {
        return appendStyle("margin", value);
    }
    
    public HtmlBuilder appendMarginStyle(String side, Object value) {
        return appendStyle("margin", side, value);
    }
    
    public HtmlBuilder appendBorderStyle(Object value) {
        return appendStyle("boder", value);
    }
    
    public HtmlBuilder appendBorderStyle(String direction, Object value) {
        return appendStyle("boder", direction, value);
    }
    
    public HtmlBuilder appendBorderRadiusStyle(Object value) {
        return appendStyle("boder-radius", value);
    }
    
    public HtmlBuilder appendBorderRadiusStyle(String direction, Object value) {
        return appendStyle("boder-radius", direction, value);
    }
    
    public HtmlBuilder appendStyle(String name, Object value) {
        return appendStyle(name, null, value);
    }
    
    public HtmlBuilder appendStyle(String name, String nameExt, Object value) {
        if (value == null) {
            return this;
        }
        
        append(name);
        if (nameExt != null) {
            append(":").append(nameExt);
        }
        return append(":").append(value).append(";");
    }
    
    public StringBuilder getStringBuilder() {
        return this.sb;
    }

    @Override
    public String toString() {
        return sb.toString();
    }
    
}
