package org.huazhi.system.syslog.common.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.huazhi.system.syslog.common.service.IParseFunction;

import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ParseFunctionFactory {
    private Map<String, IParseFunction> allFunctionMap;

    public ParseFunctionFactory(List<IParseFunction> parseFunctions) {
        if (parseFunctions == null || parseFunctions.isEmpty()) {
            return;
        }
        allFunctionMap = new HashMap<>();
        for (IParseFunction parseFunction : parseFunctions) {
            if (parseFunction.functionName() == null || parseFunction.functionName().isEmpty()) {
                continue;
            }
            allFunctionMap.put(parseFunction.functionName(), parseFunction);
        }
    }

    public IParseFunction getFunction(String functionName) {
        return allFunctionMap.get(functionName);
    }

    public boolean isBeforeFunction(String functionName) {
        return allFunctionMap.get(functionName) != null && allFunctionMap.get(functionName).executeBefore();
    }
}
