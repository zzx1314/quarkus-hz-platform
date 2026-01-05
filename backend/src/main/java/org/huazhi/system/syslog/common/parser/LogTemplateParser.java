package org.huazhi.system.syslog.common.parser;

import jakarta.interceptor.InvocationContext;

public interface LogTemplateParser {

    String parse(String template,
                 InvocationContext ctx,
                 Object resultOrException);
}
