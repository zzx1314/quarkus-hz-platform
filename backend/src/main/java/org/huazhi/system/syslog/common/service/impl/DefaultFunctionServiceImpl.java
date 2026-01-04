package org.huazhi.system.syslog.common.service.impl;

import org.huazhi.system.syslog.common.service.IFunctionService;
import org.huazhi.system.syslog.common.service.IParseFunction;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author muzhantong
 * create on 2021/2/1 5:18 下午
 */
@ApplicationScoped
public class DefaultFunctionServiceImpl implements IFunctionService {

    private final ParseFunctionFactory parseFunctionFactory;

    public DefaultFunctionServiceImpl(ParseFunctionFactory parseFunctionFactory) {
        this.parseFunctionFactory = parseFunctionFactory;
    }

    @Override
    public String apply(String functionName, Object value) {
        IParseFunction function = parseFunctionFactory.getFunction(functionName);
        if (function == null) {
            return value.toString();
        }
        return function.apply(value);
    }

    @Override
    public boolean beforeFunction(String functionName) {
        return parseFunctionFactory.isBeforeFunction(functionName);
    }
}
