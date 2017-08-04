/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import lombok.Getter;

/**
 *
 * @author david
 */
@Getter
public class ResolveContextData {
    
    private final Object value;
    private final String modelNs;

    public ResolveContextData(Object value) {
        this(value, null);
    }

    public ResolveContextData(Object value, String modelNs) {
        this.value = value;
        this.modelNs = modelNs;
    }
    
    public boolean isSynchronizeable() {
        return modelNs != null;
    }
    
}
