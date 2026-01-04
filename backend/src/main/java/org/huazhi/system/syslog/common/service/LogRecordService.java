package org.huazhi.system.syslog.common.service;

import java.lang.reflect.Method;
import java.util.*;

import org.huazhi.system.syslog.common.beans.LogRecord;
import org.huazhi.system.syslog.common.beans.LogRecordOps;
import org.huazhi.system.syslog.common.context.LogRecordContext;
import org.huazhi.system.syslog.common.service.impl.DiffParseFunction;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.interceptor.InvocationContext;

@ApplicationScoped
public class LogRecordService {

    @Inject
    IOperatorGetService operatorGetService; // 获取操作人

    @Inject
    ILogRecordService logRecordPersistenceService; // 日志持久化

    @Inject
    DiffParseFunction diffParseFunction; // 差异对比函数

    /**
     * 方法执行前上下文准备
     */
    public void beforeExecute(Collection<LogRecordOps> ops,
                              InvocationContext ctx) {
        // 初始化方法上下文 span
        LogRecordContext.putEmptySpan();

        // 保存方法参数到上下文，方便模板引用
        Method method = ctx.getMethod();
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
                boolean isSuccess = Boolean.parseBoolean(
                        parseTemplate(op.getIsSuccess(), ctx, result)
                );
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
        if (result != null && op.getSuccessLogTemplate() != null && op.getSuccessLogTemplate().contains("successCondition")) {
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

        Method method = ctx.getMethod();

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
        if (template == null) return null;

        // 支持 #_DIFF() 差异函数占位
        if (template.contains("_DIFF")) {
            template = diffParseFunction.diff(value);
        }

        // 支持 arg0、arg1 等参数占位
        for (int i = 0; i < ctx.getParameters().length; i++) {
            String placeholder = "#arg" + i;
            if (template.contains(placeholder)) {
                template = template.replace(placeholder, String.valueOf(ctx.getParameters()[i]));
            }
        }

        // 支持异常占位 #exception
        if (value instanceof Throwable) {
            template = template.replace("#exception", ((Throwable) value).getMessage());
        }

        return template;
    }
}
