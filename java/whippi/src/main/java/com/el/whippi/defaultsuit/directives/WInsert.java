/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi.defaultsuit.directives;

import com.el.whippi.ADirective;
import com.el.whippi.RenderResult;
import com.el.whippi.ResolveContext;
import com.el.whippi.ResolveContextData;
import com.el.whippi.WNode;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;

/**
 *
 * @author david
 */
public class WInsert extends ADirective {

    public WInsert() {
        super(":insert");
    }

    @Override
    protected List<WNode> process(Element element, WNode node, ResolveContext ctx, RenderResult result) {
        String baseTagName = node.getAttribute("tag") == null ? null : node.getAttribute("tag").toString().toLowerCase();
        
        ResolveContextData instanceData = ctx.get("@compositeInstance");
        if (instanceData == null) {
            result.logError("The ':insert' directive can be used only in a composite.", node);
            return new ArrayList<>();
        }
        WNode instance = (WNode) instanceData.getValue();
        if (instance == null) {
            result.logError("The ':insert' directive can be used only in a composite.", node);
            return new ArrayList<>();
        }
        
        WNode rootChild = instance;
        if (baseTagName != null) {
            boolean found = false;
            for (WNode child : instance.getChildren()) {
                if (child.getType() != WNode.EWNodeType.ELEMENT) {
                    continue;
                }
                if (baseTagName.equalsIgnoreCase(child.getTagName())) {
                    rootChild = child;
                    found = true;
                }
            }
            if (!found) {
                result.logError("Can't find child element: '" + baseTagName + "' of composite instance to insert!", node);
                return new ArrayList<>(); 
            }
        }
        
        List<WNode> res = new ArrayList<>();
        for (WNode child : rootChild.getChildren()) {
            res.add(child);
        }
        
        return res;
    }
    
}
