/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi.defaultsuit.directives;

import com.el.whippi.ADirective;
import com.el.whippi.RenderResult;
import com.el.whippi.ResolveContext;
import com.el.whippi.WNode;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author david
 */
public class WForEach extends ADirective {

    public WForEach() {
        super(":foreach");
    }

    @Override
    protected List<WNode> process(Element element, WNode node, ResolveContext ctx, RenderResult result) {
        Object model = node.getModel();
        if (model == null) {
            return new ArrayList<>();
        }
        if (!(model instanceof List)) {
            result.logError(":foreach directiva's model must be a list, but it's: " + model.getClass().getName(), node);
            return new ArrayList<>();
        }
        List modelList = (List) model;
        
        String oddVarName = "odd";
        Object oddVarNameObj = node.getAttribute("oddvarname");
        if (oddVarNameObj != null) {
            oddVarName = oddVarNameObj.toString();
        }
        
        String indexVarName = "index";
        Object indexVarNameObj = node.getAttribute("indexvarname");
        if (indexVarNameObj != null) {
            indexVarName = indexVarNameObj.toString();
        }
        
        String itemVarName = "item";
        Object itemVarNameObj = node.getAttribute("itemvarname");
        if (itemVarNameObj != null) {
            itemVarName = itemVarNameObj.toString();
        }
        
        List<WNode> res = new ArrayList<>();
        
        for (int i = 0; i < modelList.size(); i++) {
            Object modelItem = modelList.get(i);
            ResolveContext subCtx = new ResolveContext(ctx, itemVarName);
            if (node.getModelNS() != null) {
                subCtx.put(itemVarName, modelItem, node.getModelNS() + "[" + i + "]");
            } else {
                subCtx.put(itemVarName, modelItem);
            }
            subCtx.put(indexVarName, i);
            subCtx.put(oddVarName, i % 2 == 1);
            for (int j = 0; j < element.getChildNodes().getLength(); j++) {
                Node childNode = element.getChildNodes().item(j);
                List<WNode> resolvedChilds = this.resolveChild(childNode, node, subCtx, result);
                res.addAll(resolvedChilds);
            }
        }
        
        return res;
    }
    
}
