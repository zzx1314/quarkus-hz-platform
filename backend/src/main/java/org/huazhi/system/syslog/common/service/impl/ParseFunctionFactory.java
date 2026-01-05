package org.huazhi.system.syslog.common.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.huazhi.system.syslog.common.service.IParseFunction;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class ParseFunctionFactory {

    private final Map<String, IParseFunction> allFunctionMap = new HashMap<>();

    @Inject
    Instance<IParseFunction> parseFunctions;

    @PostConstruct
    void init() {
        for (IParseFunction parseFunction : parseFunctions) {
            String functionName = parseFunction.functionName();
            if (functionName == null || functionName.isEmpty()) {
                continue;
            }
            allFunctionMap.put(functionName, parseFunction);
        }
    }

    public IParseFunction getFunction(String functionName) {
        return allFunctionMap.get(functionName);
    }

    public boolean isBeforeFunction(String functionName) {
        IParseFunction function = allFunctionMap.get(functionName);
        return function != null && function.executeBefore();
    }
}
