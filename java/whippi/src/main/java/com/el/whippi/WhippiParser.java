/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import com.el.whippi.el.ExpressionResolver;
import com.el.whippi.el.ExpressionResolverException;
import com.el.whippi.el.ExpressionResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author david
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WhippiParser {

    private static long nextId = 0;

    private static synchronized long getNextId() {
        nextId = nextId + 1;
        return nextId;
    }

    static RenderResult renderPage(String url, Element root, AController<?> controller, Object model, Map<String, String> parameters) {
        RenderResult res = new RenderResult();
        WNode page = new WNode("cid-" + getNextId(), "Page", "model", null, WNode.EWNodeType.ELEMENT, null);

        ResolveContext context = new ResolveContext("model");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            context.getData().put(entry.getKey(), entry.getValue());
        }
        context.getData().put("model", model);

        boolean headFound = false;
        boolean bodyFound = false;
        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            Node node = root.getChildNodes().item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;
                String tagName = e.getTagName();
                if (tagName.equals("head")) {
                    if (headFound) {
                        res.logError("Multiple head elements found in page: " + url, page);
                        return res;
                    }
                    headFound = true;
                    renderHead(e, page, context, res);
                } else if (tagName.equals("body")) {
                    if (bodyFound) {
                        res.logError("Multiple body elements found in page: " + url, page);
                        return res;
                    }
                    bodyFound = true;
                    renderBody(e, page, context, res);
                } else {
                    res.logError("Invalid tag: '" + tagName + "' in Page: '" + url + "'! Only 'head' and 'body' is allowed.", page);
                    return res;
                }
            }
        }

        renderModel(model, res);
        renderParams(parameters, res);

        return res;
    }

    private static void renderHead(Element node, WNode page, ResolveContext context, RenderResult res) {
        // TODO resolve text content
        res.appendHead(node.getTextContent());
        renderApi(page, res);
    }

    private static void renderBody(Element node, WNode page, ResolveContext context, RenderResult res) {
        // resolve body
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node n = node.getChildNodes().item(i);
            List<WNode> nodes = resolveNode(n, page, context, res);
            page.addChildren(nodes);
        }

        for (WNode child : page.getChildren()) {
            renderNode(child, res);
        }

    }

    private static void renderApi(WNode page, RenderResult result) {
        String apiUrl = "/com/el/whippi/api/api.js";
        String api = Whippi.readResource(apiUrl);
        if (api == null) {
            throw new RuntimeException("Can't read api: " + apiUrl);
        }
        result.appendHead("\t\t<script>\n");
        result.appendHead(api);
        result.appendHead("\t\t</script>\n");
    }

    private static void renderParams(Map<String, String> params, RenderResult result) {
        String paramsJson = "{}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            paramsJson = mapper.writeValueAsString(params);
            paramsJson = paramsJson.replace("'", "\\'");
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        result.appendHead("\t\t<script>\n");
        result.appendHead("var params = JSON.parse('" + paramsJson + "');\n");
        result.appendHead("\t\t</script>\n");
    }

    private static void renderModel(Object model, RenderResult result) {
        String modelJson = null;
        if (model != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
                modelJson = mapper.writeValueAsString(model);
                modelJson = modelJson.replace("'", "\\'");
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
        result.appendHead("\t\t<script>\n");
        if (modelJson != null) {
            result.appendHead("var modelType = '" + model.getClass().getName() + "';\n");
            result.appendHead("var model = JSON.parse('" + modelJson + "');\n");
        } else {
            result.appendHead("var modelType = null;\n");
            result.appendHead("var model = null;\n");
        }
        result.appendHead("\t\t</script>\n");
    }

    static void renderNode(WNode node, RenderResult result) {
        switch (node.getType()) {
            case COMMENT:
                renderCommentNode(node, result);
                break;
            case CDATA:
                renderCDataNode(node, result);
                break;
            case TEXT:
                renderTextNode(node, result);
                break;
            case ELEMENT:
                renderElementNode(node, result);
                break;
            default:
                throw new RuntimeException("Unhandled node type: " + node.getType().name());
        }
    }

    private static void renderCommentNode(WNode node, RenderResult result) {
        // TODO implementálni
    }

    private static void renderCDataNode(WNode node, RenderResult result) {
        // TODO implementálni
    }

    private static void renderTextNode(WNode node, RenderResult result) {
        result.appendBody(node.getValue());
    }

    private static void renderElementNode(WNode node, RenderResult result) {
        String tagName = node.getTagName();

        AComponent component = Whippi.getComponent(tagName);
        if (component != null) {
            component.render(node, result);
            return;
        }

        // TODO check composites
        renderHtmlNode(node, result);
    }

    private static void renderHtmlNode(WNode node, RenderResult result) {
        String tagName = node.getTagName();

        StringBuilder sb = new StringBuilder();
        sb.append("<").append(tagName);

        sb.append(" data-cid=\"").append(node.getCid()).append("\"");

        for (Map.Entry<String, Object> entry : node.getAttributes().entrySet()) {
            sb.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
        }

        if (node.getChildren().size() > 0) {
            sb.append(">");
            result.appendBody(sb.toString());
            sb = new StringBuilder();

            for (WNode child : node.getChildren()) {
                renderNode(child, result);
            }

            sb.append("</").append(tagName).append(">");
        } else {
            sb.append("/>");
        }

        result.appendBody(sb.toString());
    }

    public static List<WNode> resolveNode(Node node, WNode parent, ResolveContext context, RenderResult result) {
        List<WNode> res = new ArrayList<>();

        if (node.getNodeType() == Node.CDATA_SECTION_NODE) {
            String value = node.getNodeValue();
            try {
                ExpressionResult expRes = ExpressionResolver.resolve(value, context);
                value = "" + expRes.getResult();
            } catch (ExpressionResolverException ex) {
                result.logError("Invalid expression: " + ex.getMessage(), parent);
            }
            WNode wnode = new WNode("cid-" + getNextId(), null, null, value, WNode.EWNodeType.CDATA, parent);
            res.add(wnode);
        } else if (node.getNodeType() == Node.COMMENT_NODE) {
            WNode wnode = new WNode("cid-" + getNextId(), null, null, node.getNodeValue(), WNode.EWNodeType.CDATA, parent);
            res.add(wnode);
        } else if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) node;
            String tagName = e.getTagName();

            String modelNS = context.getModelNS();
            Map<String, Object> attributes = new HashMap<>();
            for (int i = 0; i < e.getAttributes().getLength(); i++) {
                Node attrNode = node.getAttributes().item(i);
                String attrName = attrNode.getNodeName();
                Object attrValue = attrNode.getNodeValue();
                try {
                    ExpressionResult expRes = ExpressionResolver.resolve(attrValue.toString(), context);
                    attrValue = expRes.getResult();
                    if (attrName.equals("model")) {
                        modelNS = expRes.getModelNS();
                    }
                } catch (ExpressionResolverException ex) {
                    result.logError("Invalid expression (" + attrValue + "): " + ex.getMessage(), parent);
                }
                attributes.put(attrName, attrValue);
            }
            WNode wnode = new WNode("cid-" + getNextId(), tagName, modelNS, null, WNode.EWNodeType.ELEMENT, parent);
            wnode.getAttributes().putAll(attributes);

            ADirective directive = Whippi.getDirective(tagName);
            if (directive != null) {
                return directive.process(e, parent, context, result);
            }

            for (int i = 0; i < e.getChildNodes().getLength(); i++) {
                Node childNode = e.getChildNodes().item(i);
                wnode.addChildren(resolveNode(childNode, wnode, context, result));
            }

            res.add(wnode);
        } else if (node.getNodeType() == Node.TEXT_NODE) {
            String value = node.getNodeValue();
            try {
                ExpressionResult expRes = ExpressionResolver.resolve(value, context);
                value = "" + expRes.getResult();
            } catch (ExpressionResolverException ex) {
                result.logError("Invalid expression: " + ex.getMessage(), parent);
            }
            WNode wnode = new WNode("cid-" + getNextId(), null, null, value, WNode.EWNodeType.TEXT, parent);
            res.add(wnode);
        }

        return res;
    }

}
