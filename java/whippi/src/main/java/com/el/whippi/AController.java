/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author david
 */
public abstract class AController<$Model> {
    
    $Model callAction(String name, Object model, Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws RedirectException {
        ActionContext<$Model> ctx = new ActionContext<>(params, ($Model)model, request, response);
        
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
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            if (ex.getTargetException() instanceof RedirectException) {
                throw (RedirectException)ex.getTargetException();
            } else {
                throw new RuntimeException(ex);
            }
        }
        
        return ($Model) res;
    }
    
    public abstract $Model load(ActionContext<$Model> ctx) throws RedirectException;
    
}
