/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import com.el.whippi.defaultsuit.WButton;
import com.el.whippi.defaultsuit.WFontAwesome;
import com.el.whippi.defaultsuit.directives.ForEach;
import com.el.whippi.defaultsuit.directives.Fork;
import com.el.whippi.defaultsuit.directives.Insert;
import com.el.whippi.defaultsuit.input.WCheckBox;
import com.el.whippi.defaultsuit.input.WTextArea;
import com.el.whippi.defaultsuit.input.WTextBox;
import com.el.whippi.defaultsuit.layout.WAnchorPanel;
import com.el.whippi.defaultsuit.layout.WHBox;
import com.el.whippi.defaultsuit.layout.WVBox;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author david
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Whippi {

    private static final Logger LOG = Logger.getLogger(Whippi.class.getName());

    private static final ThreadLocal<ServletContext> servletContexts = new ThreadLocal<>();

    private static final Map<String, ADirective> directives = new HashMap<>();
    private static final Map<String, AComponent> components = new HashMap<>();

    static {
        registerDirective(new ForEach());
        registerDirective(new Fork());
        registerDirective(new Insert());
        
        registerComponent(new WFontAwesome());
        registerComponent(new WButton());
        registerComponent(new WTextBox());
        registerComponent(new WTextArea());
        registerComponent(new WCheckBox());
        registerComponent(new WAnchorPanel());
        registerComponent(new WHBox());
        registerComponent(new WVBox());
    }

    public static void registerDirective(ADirective directive) {
        directives.put(directive.getTagName(), directive);
    }

    public static void registerComponent(AComponent component) {
        components.put(component.getTagName(), component);
    }

    public static ADirective getDirective(String tagName) {
        return directives.get(tagName);
    }

    public static AComponent getComponent(String tagName) {
        return components.get(tagName);
    }

    @Getter
    private static String pageBaseUrl = "/WEB-INF/pages";
    @Getter
    private static String compositeBaseUrl = "/WEB-INF/composites";

    public static void setPageBaseUrl(String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        pageBaseUrl = url;
    }

    public static void setCompositeBaseUrl(String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        compositeBaseUrl = url;
    }

    static void handleGet(HttpServletRequest req, HttpServletResponse resp, ServletContext servletContext) throws IOException {
        long startTime = System.currentTimeMillis();
        servletContexts.set(servletContext);
        String url = pageBaseUrl + req.getServletPath();
        Document doc = readXmlFile(url);
        if (doc == null) {
            resp.setStatus(404);
            return;
        }

        Element root = doc.getDocumentElement();

        if (!root.getTagName().equals("Page")) {
            LOG.severe("Invalid page xml: '" + url + "'. The root element's name must be: 'Page'.");
            resp.setStatus(500);
            return;
        }

        String controllerClassStr = root.getAttribute("controller");
        if (controllerClassStr == null) {
            LOG.severe("Missing controller attribute of page: " + url);
            resp.setStatus(500);
            return;
        }
        Class<?> controllerClass;
        try {
            controllerClass = (Class<?>) Class.forName(controllerClassStr);
        } catch (ClassNotFoundException ex) {
            LOG.severe("Can't find controller class: " + controllerClassStr);
            resp.setStatus(500);
            return;
        }

//        String modelClassStr = root.getAttribute("modelType");
//        if (modelClassStr == null) {
//            LOG.severe("Missing modelType attribute of page: " + url);
//            resp.setStatus(500);
//            return;
//        }
//        
//        Class<?> modelClass; 
//        try {
//            modelClass = (Class<?>) Class.forName(modelClassStr);
//        } catch (ClassNotFoundException ex) {
//            LOG.severe("Can't find model class: " + modelClassStr);
//            resp.setStatus(500);
//            return;
//        }
        if (!AController.class.isAssignableFrom(controllerClass)) {
            LOG.severe("Invalid controller: '" + controllerClassStr + "'. The controller must extends the class: AController.");
            resp.setStatus(500);
            return;
        }

        AController<?> controller;
        try {
            controller = (AController<?>) controllerClass.newInstance();
        } catch (InstantiationException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            resp.setStatus(500);
            return;
        } catch (IllegalAccessException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            resp.setStatus(500);
            return;
        }

        Map<String, String> params = extractParams(req);

        Object model;
        try {
            ActionContext ctx = new ActionContext(params, null, req, resp);
            model = controller.load(ctx);
        } catch (RedirectException ex) {
            try {
                resp.sendRedirect(ex.getRedirectUrl());
            } catch (IOException ex1) {
                throw new RuntimeException(ex1);
            }
            return;
        }

        RenderResult renderResult = WhippiParser.renderPage(req.getServletPath(), root, controller, model, params);

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("    <head>\n")
                .append(renderResult.renderHead())
                .append("    </head>\n")
                .append("    <body>\n")
                .append(renderResult.renderBody())
                .append("    </body>\n")
                .append("</html>\n");

        String htmlStr = sb.toString();

        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        resp.getWriter().write(htmlStr);

        long endTime = System.currentTimeMillis();

        System.out.println(htmlStr);

        System.out.println(req.getServletPath() + " rendered in: " + (endTime - startTime) + "ms.");

    }

    static void handlePost(HttpServletRequest req, HttpServletResponse resp, ServletContext servletContext) throws IOException {
        long startTime = System.currentTimeMillis();
        servletContexts.set(servletContext);
        String url = pageBaseUrl + req.getServletPath();
        Document doc = readXmlFile(url);
        if (doc == null) {
            resp.setStatus(404);
            return;
        }

        Element root = doc.getDocumentElement();

        if (!root.getTagName().equals("Page")) {
            LOG.severe("Invalid page xml: '" + url + "'. The root element's name must be: 'Page'.");
            resp.setStatus(500);
            return;
        }

        String controllerClassStr = root.getAttribute("controller");
        if (controllerClassStr == null) {
            LOG.severe("Missing controller attribute of page: " + url);
            resp.setStatus(500);
            return;
        }
        Class<?> controllerClass;
        try {
            controllerClass = (Class<?>) Class.forName(controllerClassStr);
        } catch (ClassNotFoundException ex) {
            LOG.severe("Can't find controller class: " + controllerClassStr);
            resp.setStatus(500);
            return;
        }

        if (!AController.class.isAssignableFrom(controllerClass)) {
            LOG.severe("Invalid controller: '" + controllerClassStr + "'. The controller must extends the class: AController.");
            resp.setStatus(500);
            return;
        }

        AController<?> controller;
        try {
            controller = (AController<?>) controllerClass.newInstance();
        } catch (InstantiationException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            resp.setStatus(500);
            return;
        } catch (IllegalAccessException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            resp.setStatus(500);
            return;
        }

        Map<String, String> params = extractParams(req);
        String modelType = params.remove("modelType");
        String modelJson = params.remove("model");
        String action = params.remove("action");
        String paramsJson = params.remove("parameters");

        Object model = null;
        if (modelJson != null) {
            Class<?> modelClass;
            try {
                modelClass = Class.forName(modelType);
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
                resp.setStatus(500);
                return;
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                model = mapper.readValue(modelJson, modelClass);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
                resp.setStatus(500);
                return;
            }
        }
        
        try {
            model = controller.callAction(action, model, params, req, resp);
        } catch (RedirectException ex) {
            try {
                resp.sendRedirect(ex.getRedirectUrl());
            } catch (IOException ex1) {
                throw new RuntimeException(ex1);
            }
            return;
        }

        RenderResult renderResult = WhippiParser.renderPage(req.getServletPath(), root, controller, model, params);

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("    <head>\n")
                .append(renderResult.renderHead())
                .append("    </head>\n")
                .append("    <body>\n")
                .append(renderResult.renderBody())
                .append("    </body>\n")
                .append("</html>\n");

        String htmlStr = sb.toString();

        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        resp.getWriter().write(htmlStr);

        long endTime = System.currentTimeMillis();

        System.out.println(htmlStr);

        System.out.println(req.getServletPath() + " rendered in: " + (endTime - startTime) + "ms.");
    }

    private static Map<String, String> extractParams(HttpServletRequest req) {
        Map<String, String> res = new HashMap<>();
        Enumeration e = req.getParameterNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            res.put(name, req.getParameter(name));
        }
        return res;
    }

    static String readResource(String url) {
        InputStream is = Whippi.class.getResourceAsStream(url);
        if (is == null) {
            return null;
        }

        ByteArrayOutputStream baos;
        try {
            baos = new ByteArrayOutputStream();
            byte[] buff = new byte[4096];
            int readed;
            while ((readed = is.read(buff)) > 0) {
                baos.write(buff, 0, readed);
            }
            return new String(baos.toByteArray(), "UTF-8");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(Whippi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static Document readXmlFile(String url) {
        ServletContext servletContext = servletContexts.get();
        if (servletContext == null) {
            throw new RuntimeException("Can't find ServletContext for the current thread!");
        }
        InputStream is = servletContext.getResourceAsStream(url);
        if (is == null) {
            return null;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            return doc;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
