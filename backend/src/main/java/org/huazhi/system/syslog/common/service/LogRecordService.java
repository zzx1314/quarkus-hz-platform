package org.huazhi.system.syslog.common.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.huazhi.system.syslog.common.beans.LogRecord;
import org.huazhi.system.syslog.common.beans.LogRecordOps;
import org.huazhi.system.syslog.common.context.LogRecordContext;
import org.huazhi.system.syslog.common.service.impl.DiffParseFunction;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.interceptor.InvocationContext;

@ApplicationScoped
public class LogRecordService {
    private static final Pattern DOUBLE_BRACE_PATTERN = Pattern.compile("\\{\\{#(.*?)}}");
    private static final Pattern RESULT_PATTERN = Pattern.compile("\\{RESULT\\{#_ret}}");

    @Inject
    IOperatorGetService operatorGetService; // 获取操作人

    @Inject
    ILogRecordService logRecordPersistenceService; // 日志持久化

    @Inject
    DiffParseFunction diffParseFunction; // 差异对比函数

    /**
     * 方法执行前上下文准备
     */
    public void beforeExecute(Collection<LogRecordOps> ops, InvocationContext ctx) {
        // 初始化方法上下文 span
        LogRecordContext.putEmptySpan();

        // 保存方法参数到上下文，方便模板引用
        // Method method = ctx.getMethod();
        Object[] args = ctx.getParameters();
        for (int i = 0; i < args.length; i++) {
            LogRecordContext.putVariable("arg" + i, args[i]);
        }

        // 可选：保存操作人全局变量
        LogRecordContext.putGlobalVariable("operator", operatorGetService.getUser());
    }

    /**
     * 方法执行成功后记录日志
     */
    public void onSuccess(Collection<LogRecordOps> ops,
            InvocationContext ctx,
            Object result) {

        for (LogRecordOps op : ops) {
            if (!isConditionPassed(op, ctx, result, null)) {
                continue;
            }

            String actionTemplate = op.getSuccessLogTemplate();

            // 支持 isSuccess 条件模板
            if (op.getIsSuccess() != null && !op.getIsSuccess().isEmpty()) {
                boolean isSuccess = Boolean.parseBoolean(parseTemplate(op.getIsSuccess(), ctx, result));
                if (!isSuccess && op.getFailLogTemplate() != null && !op.getFailLogTemplate().isEmpty()) {
                    actionTemplate = op.getFailLogTemplate();
                }
            }

            if (actionTemplate == null || actionTemplate.isEmpty()) {
                continue; // 没有日志内容则跳过
            }

            // 解析模板
            String action = parseTemplate(actionTemplate, ctx, result);

            // 构建日志对象
            LogRecord logRecord = buildLogRecord(op, ctx, result, false, action);

            // 持久化
            logRecordPersistenceService.record(logRecord);
        }
    }

    /**
     * 方法执行失败后记录日志
     */
    public void onFail(Collection<LogRecordOps> ops,
            InvocationContext ctx,
            Throwable ex) {

        for (LogRecordOps op : ops) {
            if (!isConditionPassed(op, ctx, null, ex)) {
                continue;
            }

            String failTemplate = op.getFailLogTemplate();
            if (failTemplate == null || failTemplate.isEmpty()) {
                continue;
            }

            // 解析模板（支持异常信息占位）
            String action = parseTemplate(failTemplate, ctx, ex);

            // 构建日志对象
            LogRecord logRecord = buildLogRecord(op, ctx, null, true, action);

            // 持久化
            logRecordPersistenceService.record(logRecord);
        }
    }

    /**
     * 条件判断
     */
    private boolean isConditionPassed(LogRecordOps op,
            InvocationContext ctx,
            Object result,
            Throwable ex) {
        // 判断 condition 表达式
        if (op.getCondition() != null && !op.getCondition().isEmpty()) {
            String conditionValue = parseTemplate(op.getCondition(), ctx, result != null ? result : ex);
            if ("false".equalsIgnoreCase(conditionValue)) {
                return false;
            }
        }
        // 判断 successCondition（仅方法成功时）
        if (result != null && op.getSuccessLogTemplate() != null
                && op.getSuccessLogTemplate().contains("successCondition")) {
            String successValue = parseTemplate(op.getSuccessLogTemplate(), ctx, result);
            if ("false".equalsIgnoreCase(successValue)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 构建最终日志对象
     */
    private LogRecord buildLogRecord(LogRecordOps op,
            InvocationContext ctx,
            Object result,
            boolean fail,
            String action) {
        String operatorId = op.getOperatorId();
        if (operatorId == null || operatorId.isEmpty()) {
            operatorId = operatorGetService.getUser().getOperatorId();
        }

        // 支持 _DIFF 对比
        if (action.contains("_DIFF")) {
            action = diffParseFunction.diff(result);
        }

        // Method method = ctx.getMethod();

        return LogRecord.builder()
                .tenant("defaultTenant") // 可改成动态租户
                .type(op.getType())
                .subType(op.getSubType())
                .bizNo(op.getBizNo())
                .operator(operatorId)
                .extra(op.getExtra())
                .action(action)
                .fail(fail)
                .createTime(new Date())
                .build();
    }

    /**
     * 模板解析方法（支持方法参数、异常信息、差异函数等）
     */
    private String parseTemplate(String template, InvocationContext ctx, Object value) {
        if (template == null)
            return null;

        Map<String, Object> context = buildContext(ctx, value);

        // 解析 {{#...}} 模板
        Matcher matcher = DOUBLE_BRACE_PATTERN.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String expr = matcher.group(1).trim();
            Object result = evaluateExpression(expr, context);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(result == null ? "" : result.toString()));
        }
        matcher.appendTail(sb);
        template = sb.toString();

        // 解析 {RESULT{#_ret}}
        matcher = RESULT_PATTERN.matcher(template);
        sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value == null ? "" : value.toString()));
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
            // 兼容 arg0 / arg1
            context.put("arg" + i, args[i]);
            // 支持参数名（sysAuthDto / roleName）
            String paramName = parameters[i].getName();
            context.put(paramName, args[i]);
        }

        // 返回值
        context.put("_ret", value);

        // 异常
        if (value instanceof Throwable) {
            context.put("_errorMsg", ((Throwable) value).getMessage());
            context.put("exception", value);
        }

        // 操作人
        context.put("operator", operatorGetService.getUser());

        // LogRecordContext 中的变量（优先级最高）
        context.putAll(LogRecordContext.getVariables());
        return context;
    }

    private Object evaluateExpression(String expression, Map<String, Object> context) {
        // 去掉尾部可能的括号：toString() -> toString
        if (expression.endsWith("()")) {
            expression = expression.substring(0, expression.length() - 2);
        }

        String[] parts = expression.split("\\.");
        Object current = context.get(parts[0]);
        if (current == null)
            return null;

        for (int i = 1; i < parts.length; i++) {
            current = getProperty(current, parts[i]);
            if (current == null)
                return null;
        }
        return current;
    }

    private Object getProperty(Object target, String name) {
        if (target == null || name == null)
            return null;

        try {
            // 尝试 getter 方法
            String getter = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
            Method method = target.getClass().getMethod(getter);
            return method.invoke(target);
        } catch (Exception ignored) {
        }

        try {
            // 尝试字段访问
            Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception ignored) {
        }

        // 尝试直接调用 toString
        if ("toString".equals(name))
            return target.toString();

        return null;
    }

}
