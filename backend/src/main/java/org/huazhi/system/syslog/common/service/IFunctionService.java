package org.huazhi.system.syslog.common.service;

public interface IFunctionService {

    String apply(String functionName, Object value);

    boolean beforeFunction(String functionName);
}
