/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi.test;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author david
 */
@Getter
@Setter
public class IndexModel {
    
    private int clickCount = 0;
    private String text = "Clicked: 0 times";
    private boolean checked = true;
    private String textAreaModel;
    private List<String> tbModel = new ArrayList<>();

    public IndexModel() {
        tbModel.add("TextBox 1");
        tbModel.add("TextBox 2");
        tbModel.add("TextBox 3");
        tbModel.add("TextBox 4");
        tbModel.add("TextBox 5");
    }
    
}
