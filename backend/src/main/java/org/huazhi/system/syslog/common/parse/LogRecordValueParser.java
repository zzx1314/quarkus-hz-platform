package org.huazhi.system.syslog.common.parse;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.huazhi.system.syslog.common.beans.MethodExecuteResult;
import org.huazhi.system.syslog.common.service.impl.DiffParseFunction;

/**
 * Quarkus 版日志表达式解析器
 * 原 Spring SpEL 替代
 * 支持 {function{expression}} 模板解析
 */
public class LogRecordValueParser {

    private static final Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");
    public static final String COMMA = ",";

    private final LogRecordExpressionEvaluator expressionEvaluator = new LogRecordExpressionEvaluator();

    protected boolean diffSameWhetherSaveLog;

    private LogFunctionParser logFunctionParser;

    private DiffParseFunction diffParseFunction;

    /**
     * 统计字符串中某个子串出现次数
     */
    public static int strCount(String srcText, String findText) {
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            index += findText.length();
            count++;
        }
        return count;
    }

    /**
     * 单个模板处理
     */
    public String singleProcessTemplate(MethodExecuteResult methodExecuteResult,
                                        String template,
                                        Map<String, String> beforeFunctionNameAndReturnMap) {
        Map<String, String> map = processTemplate(Collections.singletonList(template),
                methodExecuteResult, beforeFunctionNameAndReturnMap);
        return map.get(template);
    }

    /**
     * 批量模板处理
     */
    public Map<String, String> processTemplate(Collection<String> templates,
                                               MethodExecuteResult methodExecuteResult,
                                               Map<String, String> beforeFunctionNameAndReturnMap) {

        Map<String, String> expressionValues = new HashMap<>();
        LogRecordEvaluationContext evalContext = expressionEvaluator.createEvaluationContext(
                methodExecuteResult.getMethod(),
                methodExecuteResult.getArgs(),
                methodExecuteResult.getTargetClass(),
                methodExecuteResult.getResult(),
                methodExecuteResult.getErrorMsg()
        );

        for (String template : templates) {
            if (template.contains("{")) {
                Matcher matcher = pattern.matcher(template);
                StringBuffer parsedStr = new StringBuffer();
                boolean sameDiff = false;

                while (matcher.find()) {
                    String expression = matcher.group(2);
                    String functionName = matcher.group(1);

                    if ("diff".equals(functionName)) { // DiffParseFunction 函数名
                        expression = getDiffFunctionValue(evalContext, expression);
                        sameDiff = "".equals(expression);
                    } else {
                        Object value = expressionEvaluator.parseExpression(expression, expressionEvaluator.createMethodKey(methodExecuteResult.getTargetClass(), methodExecuteResult.getMethod()), evalContext);
                        expression = logFunctionParser.getFunctionReturnValue(beforeFunctionNameAndReturnMap, value, expression, functionName);
                    }

                    matcher.appendReplacement(parsedStr, Matcher.quoteReplacement(expression == null ? "" : expression));
                }

                matcher.appendTail(parsedStr);
                expressionValues.put(template,
                        recordSameDiff(sameDiff) ? parsedStr.toString() : template);

            } else {
                expressionValues.put(template, template);
            }
        }

        return expressionValues;
    }

    private boolean recordSameDiff(boolean sameDiff) {
        if (diffSameWhetherSaveLog) {
            return true;
        }
        return !sameDiff;
    }

    private String getDiffFunctionValue(LogRecordEvaluationContext context, String expression) {
        String[] params = parseDiffFunction(expression);
        if (params.length == 1) {
            Object targetObj = expressionEvaluator.parseExpression(params[0], null, context);
            return diffParseFunction.diff(targetObj);
        } else if (params.length == 2) {
            Object sourceObj = expressionEvaluator.parseExpression(params[0], null, context);
            Object targetObj = expressionEvaluator.parseExpression(params[1], null, context);
            return diffParseFunction.diff(sourceObj, targetObj);
        }
        return expression;
    }

    private String[] parseDiffFunction(String expression) {
        if (expression.contains(COMMA) && strCount(expression, COMMA) == 1) {
            return expression.split(COMMA);
        }
        return new String[]{expression};
    }

    /**
     * 处理前置函数模板
     */
    public Map<String, String> processBeforeExecuteFunctionTemplate(Collection<String> templates,
                                                                    Class<?> targetClass,
                                                                    Method method,
                                                                    Object[] args) {
        Map<String, String> functionNameAndReturnValueMap = new HashMap<>();
        LogRecordEvaluationContext evalContext = expressionEvaluator.createEvaluationContext(
                method, args, targetClass, null, null
        );

        for (String template : templates) {
            if (template.contains("{")) {
                Matcher matcher = pattern.matcher(template);
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    if (expression.contains("_ret") || expression.contains("_errorMsg")) {
                        continue;
                    }
                    String functionName = matcher.group(1);
                    if (logFunctionParser.beforeFunction(functionName)) {
                        Object value = expressionEvaluator.parseExpression(expression, null, evalContext);
                        String functionReturnValue = logFunctionParser.getFunctionReturnValue(null, value, expression, functionName);
                        String functionCallInstanceKey = logFunctionParser.getFunctionCallInstanceKey(functionName, expression);
                        functionNameAndReturnValueMap.put(functionCallInstanceKey, functionReturnValue);
                    }
                }
            }
        }

        return functionNameAndReturnValueMap;
    }

    // -------------------- Setter --------------------

    public void setLogFunctionParser(LogFunctionParser logFunctionParser) {
        this.logFunctionParser = logFunctionParser;
    }

    public void setDiffParseFunction(DiffParseFunction diffParseFunction) {
        this.diffParseFunction = diffParseFunction;
    }

    public void setDiffSameWhetherSaveLog(boolean diffSameWhetherSaveLog) {
        this.diffSameWhetherSaveLog = diffSameWhetherSaveLog;
    }
}

