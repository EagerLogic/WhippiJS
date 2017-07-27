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
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author david
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResolveContext {
    
    private final Map<String, Object> data = new HashMap<>();
    private String modelNS;
    
    public ResolveContext copy() {
        ResolveContext res = new ResolveContext(modelNS);
        res.data.putAll(data);
        return res;
    }
    
}
