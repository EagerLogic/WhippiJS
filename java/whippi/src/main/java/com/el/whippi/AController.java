/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 * @author david
 */
public abstract class AController<$Model> {
    
    protected void redirect(String url) throws RedirectException {
        throw new RedirectException(url);
    }
    
    $Model callAction(String name, Object model, Map<String, String> params) {
        ActionContext<$Model> ctx = new ActionContext<>(params, ($Model)model);
        
        Method method;
        try {
            method = this.getClass().getMethod(name, ActionContext.class);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException("Can't find action method: '" + name + "' in controller: " + this.getClass().getName());
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
        
        Object res;
        try {
            res = method.invoke(this, ctx);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
        
        return ($Model) res;
    }
    
    public abstract $Model load(Map<String, String> params) throws RedirectException;
    
}
