/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import lombok.AllArgsConstructor;

/**
 *
 * @author david
 */
@AllArgsConstructor
public class StyleAppender {
    
    private final StringBuilder sb;

    public StyleAppender() {
        this(new StringBuilder());
    }
    
    public StyleAppender appendWidth(Object value) {
        return appendStyle("width", value);
    }
    
    public StyleAppender appendHeight(Object value) {
        return appendStyle("height", value);
    }
    
    public StyleAppender appendHAlign(Object value) {
        return appendStyle("text-align", value);
    }
    
    public StyleAppender appendVAlign(Object value) {
        return appendStyle("vertical-align", value);
    }
    
    public StyleAppender appendPadding(Object value) {
        return appendStyle("padding", null, value);
    }
    
    public StyleAppender appendPadding(String side, Object value) {
        return appendStyle("padding", side, value);
    }
    
    public StyleAppender appendMargin(Object value) {
        return appendStyle("margin", value);
    }
    
    public StyleAppender appendMargin(String side, Object value) {
        return appendStyle("margin", side, value);
    }
    
    public StyleAppender appendBorder(Object value) {
        return appendStyle("boder", value);
    }
    
    public StyleAppender appendBorder(String direction, Object value) {
        return appendStyle("boder", direction, value);
    }
    
    public StyleAppender appendBorderRadius(Object value) {
        return appendStyle("boder-radius", value);
    }
    
    public StyleAppender appendBorderRadius(String direction, Object value) {
        return appendStyle("boder-radius", direction, value);
    }
    
    public StyleAppender appendStyle(String name, Object value) {
        return appendStyle(name, null, value);
    }
    
    public StyleAppender appendStyle(String name, String nameExt, Object value) {
        if (value == null) {
            return this;
        }
        
        append(name);
        if (nameExt != null) {
            append(":").append(nameExt);
        }
        return append(":").append(value).append(";");
    }
    
    public StyleAppender append(Object value) {
        sb.append(value);
        return this;
    }
    
}
