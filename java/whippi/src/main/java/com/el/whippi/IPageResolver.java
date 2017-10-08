/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ipacsdavid
 */
public interface IPageResolver {
    
    public String resolvePage(HttpServletRequest req);
    
    
}
