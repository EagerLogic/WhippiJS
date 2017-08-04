/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author david
 */
@AllArgsConstructor
public class ResolveContext {
    
    private final Map<String, ResolveContextData> data = new HashMap<>();
    
    @Getter private final ResolveContext parent;
    @Getter private final String modelAlias;
    
    public ResolveContextData get(String key) {
        ResolveContextData res = this.data.get(key.toLowerCase());
        
        if (res == null && parent != null) {
            res = this.parent.get(key);
        }
        
        return res;
    }
    
    public void put(String key, Object value) {
        this.put(key, value, null);
    }
    
    public void put(String key, Object value, String modelNS) {
        this.data.put(key.toLowerCase(), new ResolveContextData(value, modelNS));
    }
    
}
