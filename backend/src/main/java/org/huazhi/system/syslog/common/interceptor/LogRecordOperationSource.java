package org.huazhi.system.syslog.common.interceptor;


import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.huazhi.system.syslog.common.annotation.LogRecord;
import org.huazhi.system.syslog.common.beans.LogRecordOps;


public class LogRecordOperationSource {

    /**
     * Cache for interface method lookup
     */
    private static final Map<Method, Method> INTERFACE_METHOD_CACHE =
            new ConcurrentHashMap<>(256);

    /**
     * Parse LogRecordOps from method / interface / class
     */
    public Collection<LogRecordOps> computeLogRecordOperations(
            Method method, Class<?> targetClass) {

        // Only public methods are supported
        if (!Modifier.isPublic(method.getModifiers())) {
            return Collections.emptyList();
        }

        Set<LogRecordOps> result = new HashSet<>();

        //  Method itself
        parse(method, result);

        // Interface method (if exists)
        Method interfaceMethod = getInterfaceMethodIfPossible(method);
        if (interfaceMethod != method) {
            parse(interfaceMethod, result);
        }

        // Class-level annotation
        if (targetClass != null) {
            parse(targetClass, result);
        }

        return result;
    }

    /**
     * Try to find corresponding interface method
     */
    private Method getInterfaceMethodIfPossible(Method method) {
        if (method.getDeclaringClass().isInterface()) {
            return method;
        }

        return INTERFACE_METHOD_CACHE.computeIfAbsent(method, m -> {
            for (Class<?> ifc : m.getDeclaringClass().getInterfaces()) {
                try {
                    return ifc.getMethod(m.getName(), m.getParameterTypes());
                } catch (NoSuchMethodException ignored) {
                }
            }
            return m;
        });
    }

    /**
     * Parse @LogRecord annotations from AnnotatedElement
     */
    private void parse(AnnotatedElement element, Set<LogRecordOps> result) {
        LogRecord[] records = element.getAnnotationsByType(LogRecord.class);
        if (records.length == 0) {
            return;
        }

        for (LogRecord record : records) {
            LogRecordOps ops = convert(record);
            validate(element, ops);
            result.add(ops);
        }
    }

    /**
     * Convert annotation to LogRecordOps
     */
    private LogRecordOps convert(LogRecord record) {
        return LogRecordOps.builder()
                .successLogTemplate(record.success())
                .failLogTemplate(record.fail())
                .type(record.type())
                .bizNo(record.bizNo())
                .operatorId(record.operator())
                .subType(record.subType())
                .extra(record.extra())
                .condition(record.condition())
                .isSuccess(record.successCondition())
                .build();
    }

    /**
     * Validate LogRecord configuration
     */
    private void validate(AnnotatedElement element, LogRecordOps recordOps) {
        if (!hasText(recordOps.getSuccessLogTemplate())
                && !hasText(recordOps.getFailLogTemplate())) {

            throw new IllegalStateException(
                    "Invalid @LogRecord configuration on " + element +
                    ": either success or fail template must be set"
            );
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
