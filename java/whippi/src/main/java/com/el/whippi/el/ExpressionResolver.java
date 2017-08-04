/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.el.whippi.el;

import com.el.whippi.ResolveContext;
import com.el.whippi.ResolveContextData;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author david
 */
public class ExpressionResolver {

    public static ExpressionResult resolve(String input, ResolveContext context) throws ExpressionResolverException {
        if (isReferenceExpression(input)) {
            return resolveReferenceString(input, context);
        }

        if (isActionExpression(input)) {
            return resolveActionString(input, context);
        }

        if (isProcessExpression(input)) {
            return resolveProcessString(input, context);
        }

        ExpressionResult res = new ExpressionResult();
        res.setResult(input);
        int expEndIndex = input.lastIndexOf("}");
        if (expEndIndex > -1) {
            int expStartIndex = input.indexOf("${");
            if (expStartIndex < 0) {
                expStartIndex = input.indexOf("@{");
            }
            if (expStartIndex < 0) {
                expStartIndex = input.indexOf("#{");
            }
            if (expStartIndex > -1 && expStartIndex < expEndIndex) {
                String exp = input.substring(expStartIndex, expEndIndex + 1);
                ExpressionResult expRes = resolve(exp, context);
                res.setResult(input.substring(0, expStartIndex) + expRes.getResult() + input.substring(expEndIndex + 1));
            }
        }
        
        res.setResult(unescape(res.getResult()));

        return res;
    }

    public static Object unescape(Object input) {
        if (!(input instanceof String)) {
            return input;
        }
        String res = (String) input;
        res = res.replace("$\\", "$");
        res = res.replace("@\\", "@");
        res = res.replace("#\\", "#");
        return res;
    }

    public static ExpressionResult resolveReferenceString(String input, ResolveContext context) throws ExpressionResolverException {
        input = unpackExpression(input);
        ExpressionResult res = new ExpressionResult();
        
        String firstPart = input;
        int dotIndex = firstPart.indexOf(".");
        int bracketIndex = firstPart.indexOf("[");
        int cutIndex = -1;
        if (dotIndex > -1) {
            cutIndex = dotIndex;
        }
        if (bracketIndex > -1 & bracketIndex < cutIndex) {
            cutIndex = bracketIndex;
        }
        if (cutIndex > -1) {
            firstPart = firstPart.substring(0, cutIndex);
        }
        
        ResolveContextData modelRef = context.get(firstPart);
        if (modelRef != null && modelRef.isSynchronizeable()) {
            if (firstPart.equals("input")) {
                res.setModelNS(modelRef.getModelNs());
            } else {
                res.setModelNS(modelRef.getModelNs() + input.substring(firstPart.length()));
            }
        }

        res.setResult(resolveReferenceExpression(input, context));

        return res;
    }

    public static ExpressionResult resolveActionString(String input, ResolveContext context) throws ExpressionResolverException {
        if (!isActionExpression(input)) {
            throw new ExpressionResolverException("Not an action expression: '" + input + "'!");
        }
        
        input = unpackExpression(input);
        ExpressionResult resolveRes = resolve(input, context);
        input = "" + resolveRes.getResult();
        
        ExpressionResult res = new ExpressionResult();
        res.setResult("whippi.callAction('" + input + "');");
        
        return res;
    }

    public static ExpressionResult resolveProcessString(String input, ResolveContext context) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    private static String unpackExpression(String input) {
        return input.substring(2, input.length() - 1);
    }

    public static boolean isReferenceExpression(String input) {
        return input.startsWith("${") && input.endsWith("}");
    }

    public static boolean isActionExpression(String input) {
        return input.startsWith("@{") && input.endsWith("}");
    }

    public static boolean isProcessExpression(String input) {
        return input.startsWith("#{") && input.endsWith("}");
    }

    public static Object resolveReferenceExpression(String expression, ResolveContext context) throws ExpressionResolverException {
        char[] chars = expression.toCharArray();
        Object currentContext = context;
        StringBuilder currentRef = new StringBuilder();
        try {
            for (char c : chars) {
                if (c == '.') {
                    currentContext = resolveContext(currentRef.toString(), currentContext);
                    currentRef = new StringBuilder();
                } else if (c == '[') {
                    currentContext = resolveContext(currentRef.toString(), currentContext);
                    currentRef = new StringBuilder();
                    currentRef.append("[");
                } else if (c == ']') {
                    currentRef.append("]");
                    currentContext = resolveContext(currentRef.toString(), currentContext);
                    currentRef = new StringBuilder();
                } else {
                    currentRef.append(c);
                }
            }
            if (currentRef.length() > 0) {
                currentContext = resolveContext(currentRef.toString(), currentContext);
            }
        } catch (ExpressionResolverException ex) {
            throw new ExpressionResolverException("Invalid expression! See cause exception for details.", ex);
        }

        return currentContext;
    }

    private static Object resolveContext(String ref, Object context) throws ExpressionResolverException {
        if (ref.startsWith("[")) {
            return resolveArrayAccessContext(ref, context);
        }

        return readField(ref, context);
    }

    private static Object resolveArrayAccessContext(String ref, Object context) throws ExpressionResolverException {
        if (context == null) {
            return null;
        }

        ref = ref.substring(1, ref.length() - 1);
        if (ref.startsWith("'") && ref.endsWith("'")) {
            String strRef = ref.substring(0, ref.length() - 1);
            return readField(strRef, context);
        }

        Integer intRef;
        try {
            intRef = Integer.parseInt(ref);
        } catch (Exception ex) {
            throw new ExpressionResolverException("Invalid array index: '" + ref + "'. It must be an integer or a String.");
        }

        if (context instanceof Map) {
            return ((Map) context).get(intRef);
        }

        if (context instanceof Collection) {
            return ((Collection) context).toArray()[intRef];
        }

        if (context.getClass().isArray()) {
            return Array.get(context, intRef);
        }

        throw new ExpressionResolverException("Can't read class: '" + context.getClass().getName() + "' as an array!");
    }

    private static Object readField(String name, Object context) throws ExpressionResolverException {
        if (context == null) {
            return null;
        }

        if (context instanceof Map) {
            return ((Map) context).get(name);
        }
        
        if (context instanceof ResolveContext) {
            ResolveContextData data = ((ResolveContext)context).get(name);
            if (data != null) {
                return data.getValue();
            }
            return null;
        }

        Class<?> clazz = context.getClass();

        // try field name
        try {
            Field field = clazz.getField(name);
            try {
                return field.get(context);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (NoSuchFieldException ex) {
            // nothing to do here
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }

        // try method name
        try {
            Method method = clazz.getMethod(name);
            try {
                return method.invoke(context);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (NoSuchMethodException ex) {
            // nothing to do here
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }

        String capName = name.substring(0, 1).toUpperCase() + name.substring(1);

        // try get method
        try {
            Method method = clazz.getMethod("get" + capName);
            try {
                return method.invoke(context);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (NoSuchMethodException ex) {
            // nothing to do here
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }

        // try is method
        try {
            Method method = clazz.getMethod("is" + capName);
            try {
                return method.invoke(context);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (NoSuchMethodException ex) {
            // nothing to do here
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }

        throw new ExpressionResolverException("Can't find field: '" + name + "' of object: '" + clazz.getName() + "'.");
    }

}
