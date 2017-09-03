/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author david
 */
@AllArgsConstructor
public class WNode {
    
    public static enum EWNodeType {
        ELEMENT,
        CDATA,
        COMMENT,
        TEXT;
    }
    
    @Getter
    private final String cid;
    @Getter
    private final String tagName;
    @Getter
    private final String modelNS; 
    @Getter
    private final String value; 
    @Getter
    private final EWNodeType type;
    @Getter
    private WNode parent;
    @Getter
    private final List<WNode> children = new ArrayList<>();
    @Getter
    private final Map<String, Object> attributes = new HashMap<>();
    
    public Object getModel() {
        return attributes.get("model");
    }
    
    public Object getAttribute(String name) {
        return attributes.get(name.toLowerCase());
    }
    
    public String getAttributeString(String name) {
        Object obj = getAttribute(name);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }
    
    public void addChild(WNode child) {
        this.children.add(child);
    }
    
    public void addChildren(Collection<WNode> children) {
        this.children.addAll(children);
    }
    
}
