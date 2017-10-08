/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author david
 */
public final class RenderResult {
    
    static enum ERenderLogLevel {
        INFO,
        WARNING,
        ERROR;
    }
    
    @AllArgsConstructor
    @Getter
    static class RenderLogItem {
        private final ERenderLogLevel level;
        private final String message;
        private final WNode node;
    }
    
    private final HtmlBuilder head = new HtmlBuilder();
    private final HtmlBuilder body = new HtmlBuilder();
    private Map<String, String> headOnce = new HashMap<>();
    private Map<String, String> bodyOnce = new HashMap<>();
    private List<RenderLogItem> log = new ArrayList<>();
    @Getter private boolean containsError = false;
    private int dialogCounter = 0;
    
    public int getNextDialogNumber() {
        return ++dialogCounter;
    }
    
    public String renderHead() {
        StringBuilder res = new StringBuilder();
        res.append(head);
        for (String item : headOnce.values()) {
            res.append("\n\n");
            res.append(item);
        }
        res.append("\n");
        return res.toString();
    }
    
    public String renderBody() {
        StringBuilder res = new StringBuilder();
        res.append(body);
        for (String item : bodyOnce.values()) {
            res.append("\n\n");
            res.append(item);
        }
        res.append("\n");
        return res.toString();
    }
    
    public HtmlBuilder getHeadBuilder() {
        return this.head;
    }
    
    public HtmlBuilder getBodyBuilder() {
        return this.body;
    }
    
    public void appendHead(String headStr) {
        this.head.append(headStr);
    }
    
    public void appendHeadOnce(String key, String headStr) {
        this.headOnce.put(key, headStr);
    }
    
    public void appendBody(String bodyStr) {
        this.body.append(bodyStr);
    }
    
    public void appendBodyOnce(String key, String bodyStr) {
        this.bodyOnce.put(key, bodyStr);
    }
    
    public void logInfo(String message, WNode node) {
        this.log(ERenderLogLevel.INFO, message, node);
    }
    
    public void logWarning(String message, WNode node) {
        this.log(ERenderLogLevel.INFO, message, node);
    }
    
    public void logError(String message, WNode node) {
        this.log(ERenderLogLevel.INFO, message, node);
    }
    
    public void log(ERenderLogLevel level, String message, WNode node) {
        this.log.add(new RenderLogItem(level, message, node));
        if (level == ERenderLogLevel.ERROR) {
            containsError = true;
        }
    }
    
    List<RenderLogItem> getLog() {
        return this.log;
    }
    
    public final void appendAttribute(String name, String value) {
        body.append(" ").append(name).append("=\"").append(value).append("\"");
    }
    
}
