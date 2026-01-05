package org.huazhi.system.syslog.common.parser;

import org.huazhi.system.syslog.common.service.IFunctionService;

import java.util.Map;

public class LogFunctionParser {

    private IFunctionService functionService;

    public LogFunctionParser(IFunctionService functionService) {
        this.functionService = functionService;
    }

    public String getFunctionReturnValue(Map<String, String> beforeFunctionNameAndReturnMap,
            Object value,
            String expression,
            String functionName) {
        if (functionName == null || functionName.isEmpty()) {
            return value == null ? "" : value.toString();
        }

        String functionCallInstanceKey = getFunctionCallInstanceKey(functionName, expression);
        if (beforeFunctionNameAndReturnMap != null
                && beforeFunctionNameAndReturnMap.containsKey(functionCallInstanceKey)) {
            return beforeFunctionNameAndReturnMap.get(functionCallInstanceKey);
        } else {
            return functionService.apply(functionName, value);
        }
    }

    public String getFunctionCallInstanceKey(String functionName, String paramExpression) {
        return functionName + paramExpression;
    }

    public boolean beforeFunction(String functionName) {
        return functionService.beforeFunction(functionName);
    }

    public void setFunctionService(IFunctionService functionService) {
        this.functionService = functionService;
    }
}
