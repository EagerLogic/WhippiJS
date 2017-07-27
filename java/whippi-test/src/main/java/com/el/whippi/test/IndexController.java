/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi.test;

import com.el.whippi.AController;
import com.el.whippi.ActionContext;
import com.el.whippi.RedirectException;
import java.util.Map;

/**
 *
 * @author david
 */
public class IndexController extends AController<IndexModel> {

    @Override
    public IndexModel load(Map<String, String> params) throws RedirectException {
        return new IndexModel();
    }
    
    public IndexModel onClickmeClicked(ActionContext<IndexModel> ctx) {
        IndexModel model = ctx.getModel();
        model.setClickCount(model.getClickCount() + 1);
        model.setText("Clicked: " + model.getClickCount() + " times.");
        return model; 
    }
    
}
