package org.huazhi.system.syslog.common.parser;

import org.huazhi.system.syslog.common.service.IFunctionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.interceptor.InvocationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class LogRecordValueParser {

    // 统一解析 {{#funcName(args)}} 或 {{#var.field}}
    private static final Pattern FUNCTION_PATTERN = Pattern.compile("\\{\\{#(.*?)}}");

    @Inject
    DiffParseFunction diffParseFunction;

    @Inject
    IFunctionService functionService;

    private LogFunctionParser logFunctionParser;

    @Inject
    public void setLogFunctionParser() {
        this.logFunctionParser = new LogFunctionParser(functionService);
    }

    public String parse(String template, InvocationContext ctx, Object value) {
        if (template == null) return null;

        Map<String, Object> context = buildContext(ctx, value);

        // 解析 {{#...}}
        Matcher matcher = FUNCTION_PATTERN.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String expr = matcher.group(1).trim();

            String functionName = null;
            String paramExpr = expr;

            // 检查是否函数调用形式：funcName(args)
            int parenIndex = expr.indexOf('(');
            if (parenIndex > 0 && expr.endsWith(")")) {
                functionName = expr.substring(0, parenIndex).trim();
                paramExpr = expr.substring(parenIndex + 1, expr.length() - 1).trim();
            }

            // 获取参数值
            Object paramValue = evaluateExpression(paramExpr, context);

            // 调用函数解析
            String resolved = logFunctionParser.getFunctionReturnValue(null, paramValue, paramExpr, functionName);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(resolved == null ? "" : resolved));
        }
        matcher.appendTail(sb);
        template = sb.toString();

        // 支持 _DIFF 占位
        if (template.contains("_DIFF")) {
            template = diffParseFunction.diff(value);
        }

        return template;
    }

    private Map<String, Object> buildContext(InvocationContext ctx, Object value) {
        Map<String, Object> context = new HashMap<>();
        Method method = ctx.getMethod();
        Object[] args = ctx.getParameters();
        java.lang.reflect.Parameter[] parameters = method.getParameters();

        for (int i = 0; i < args.length; i++) {
            context.put("arg" + i, args[i]);
            context.put(parameters[i].getName(), args[i]);
        }

        context.put("_ret", value);
        if (value instanceof Throwable) {
            context.put("_errorMsg", ((Throwable) value).getMessage());
            context.put("exception", value);
        }

        return context;
    }

    private Object evaluateExpression(String expression, Map<String, Object> context) {
        String[] parts = expression.split("\\.");
        Object current = context.get(parts[0]);
        if (current == null) return null;

        for (int i = 1; i < parts.length; i++) {
            current = getProperty(current, parts[i]);
            if (current == null) return null;
        }
        return current;
    }

    private Object getProperty(Object target, String name) {
        if (target == null || name == null) return null;

        try {
            String getter = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
            Method method = target.getClass().getMethod(getter);
            return method.invoke(target);
        } catch (Exception ignored) {
        }

        try {
            Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception ignored) {
        }

        if ("toString".equals(name)) return target.toString();

        return null;
    }
}
