package org.huazhi.system.syslog.common.beans;

import java.lang.reflect.Method;

public class MethodExecuteResult {
    private boolean success;
    private Throwable throwable;
    private String errorMsg;

    private Object result;
    private final Method method;
    private final Object[] args;
    private final Class<?> targetClass;

    public MethodExecuteResult(Method method, Object[] args, Class<?> targetClass) {
        this.method = method;
        this.args = args;
        this.targetClass = targetClass;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Object getResult() {
        return result;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    
}
