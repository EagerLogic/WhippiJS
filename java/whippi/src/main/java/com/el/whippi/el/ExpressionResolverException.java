/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi.el;

/**
 *
 * @author david
 */
public class ExpressionResolverException extends Exception {

    public ExpressionResolverException() {
    }

    public ExpressionResolverException(String message) {
        super(message);
    }

    public ExpressionResolverException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionResolverException(Throwable cause) {
        super(cause);
    }
    
}
