/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author david
 */
@AllArgsConstructor
@Getter
@Setter
public class RedirectException extends RuntimeException {
    
    private final String redirectUrl;
    
}
