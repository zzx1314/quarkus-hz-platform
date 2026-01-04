package org.huazhi.system.syslog.common.parse;

import java.util.Map;

import org.huazhi.system.syslog.common.service.IFunctionService;


public class LogFunctionParser {

    private IFunctionService functionService;

    // 构造函数
    public LogFunctionParser(IFunctionService functionService) {
        this.functionService = functionService;
    }

    /**
     * 获取函数返回值
     *
     * @param beforeFunctionNameAndReturnMap 调用缓存
     * @param value                          函数入参值
     * @param expression                     参数表达式
     * @param functionName                    函数名
     * @return 函数返回值
     */
    public String getFunctionReturnValue(Map<String, String> beforeFunctionNameAndReturnMap, Object value, String expression, String functionName) {
        if (functionName == null || functionName.isEmpty()) {
            return value == null ? "" : value.toString();
        }

        String functionReturnValue = "";
        String functionCallInstanceKey = getFunctionCallInstanceKey(functionName, expression);

        if (beforeFunctionNameAndReturnMap != null && beforeFunctionNameAndReturnMap.containsKey(functionCallInstanceKey)) {
            functionReturnValue = beforeFunctionNameAndReturnMap.get(functionCallInstanceKey);
        } else {
            functionReturnValue = functionService.apply(functionName, value);
        }

        return functionReturnValue;
    }

    /**
     * 获取函数调用缓存 Key
     * 唯一标识：函数名 + 参数表达式
     */
    public String getFunctionCallInstanceKey(String functionName, String paramExpression) {
        return functionName + paramExpression;
    }

    // Setter，可选，如果不使用构造函数注入
    public void setFunctionService(IFunctionService functionService) {
        this.functionService = functionService;
    }

    /**
     * 调用函数前置判断
     */
    public boolean beforeFunction(String functionName) {
        return functionService.beforeFunction(functionName);
    }
}