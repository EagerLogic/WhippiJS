/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author david
 */
@AllArgsConstructor
@Getter
public final class ActionContext<$Model> {
    
    private final Map<String, String> params;
    private final $Model model;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    
}
