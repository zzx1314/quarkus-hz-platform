package org.huazhi.system.syslog.common.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;

import org.huazhi.system.syslog.common.annotation.LogRecord;
import org.huazhi.system.syslog.common.beans.LogRecordOps;
import org.huazhi.system.syslog.common.service.LogRecordService;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@LogRecord
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LogRecordCdiInterceptor {

    @Inject
    LogRecordOperationSource operationSource;

    @Inject
    LogRecordService logRecordService;

    @AroundInvoke
    public Object around(InvocationContext ctx) throws Exception {

        Method method = ctx.getMethod();
        Class<?> targetClass = ctx.getTarget().getClass();

        //  解析 @LogRecord（支持 repeatable）
        Collection<LogRecordOps> operations =
                operationSource.computeLogRecordOperations(method, targetClass);

        if (operations.isEmpty()) {
            return ctx.proceed();
        }

        // BEFORE
        logRecordService.beforeExecute(operations, ctx);

        try {
            Object result = ctx.proceed();

            // SUCCESS
            logRecordService.onSuccess(operations, ctx, result);
            return result;

        } catch (Throwable ex) {
            // FAIL
            logRecordService.onFail(operations, ctx, ex);
            throw ex;
        }
    }
}
