package org.huazhi.system.syslog.common.service;

import java.util.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.interceptor.InvocationContext;

import org.huazhi.system.syslog.common.beans.LogRecord;
import org.huazhi.system.syslog.common.beans.LogRecordOps;
import org.huazhi.system.syslog.common.context.LogRecordContext;
import org.huazhi.system.syslog.common.parser.DiffParseFunction;
import org.huazhi.system.syslog.common.parser.LogRecordValueParser;

@ApplicationScoped
public class LogRecordService {

    @Inject
    IOperatorGetService operatorGetService; // 获取操作人

    @Inject
    ILogRecordService logRecordPersistenceService; // 日志持久化

    @Inject
    DiffParseFunction diffParseFunction; // 差异对比函数

    @Inject
    LogRecordValueParser logRecordValueParser; // 模板解析器

    /**
     * 方法执行前上下文准备
     */
    public void beforeExecute(Collection<LogRecordOps> ops, InvocationContext ctx) {
    LogRecordContext.putEmptySpan();

    Object[] args = ctx.getParameters();
    for (int i = 0; i < args.length; i++) {
        LogRecordContext.putVariable("arg" + i, args[i]);
        LogRecordContext.putVariable(ctx.getMethod().getParameters()[i].getName(), args[i]);
    }

    LogRecordContext.putGlobalVariable("operator", operatorGetService.getUser());

    // === 新增：方法执行前函数解析 ===
    if (ops != null && !ops.isEmpty()) {
        for (LogRecordOps op : ops) {
            List<String> templates = Arrays.asList(
                    op.getSuccessLogTemplate(),
                    op.getFailLogTemplate(),
                    op.getBizNo(),
                    op.getExtra(),
                    op.getType(),
                    op.getSubType()
            );
            Map<String, String> beforeFunctionResults =
                    logRecordValueParser.processBeforeExecuteFunctionTemplate(
                            templates, ctx.getTarget().getClass(), ctx.getMethod(), args
                    );
            // 将结果写入上下文
            if (beforeFunctionResults != null) {
                beforeFunctionResults.forEach((k, v) -> LogRecordContext.putVariable(k, v));
            }
        }
    }
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
                        logRecordValueParser.parse(op.getIsSuccess(), ctx, result)
                );
                if (!isSuccess && op.getFailLogTemplate() != null && !op.getFailLogTemplate().isEmpty()) {
                    actionTemplate = op.getFailLogTemplate();
                }
            }

            if (actionTemplate == null || actionTemplate.isEmpty()) {
                continue; // 没有日志内容则跳过
            }

            // 使用 LogRecordValueParser 解析模板
            String action = logRecordValueParser.parse(actionTemplate, ctx, result);

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

            // 使用 LogRecordValueParser 解析模板（支持异常信息占位）
            String action = logRecordValueParser.parse(failTemplate, ctx, ex);

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
            String conditionValue = logRecordValueParser.parse(op.getCondition(), ctx, result != null ? result : ex);
            if ("false".equalsIgnoreCase(conditionValue)) {
                return false;
            }
        }

        // 判断 successCondition（仅方法成功时）
        if (result != null && op.getSuccessLogTemplate() != null
                && op.getSuccessLogTemplate().contains("successCondition")) {
            String successValue = logRecordValueParser.parse(op.getSuccessLogTemplate(), ctx, result);
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

        // 支持 _DIFF 对比（已经在 LogRecordValueParser 处理）
        return LogRecord.builder()
                .tenant("defaultTenant")
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
}
