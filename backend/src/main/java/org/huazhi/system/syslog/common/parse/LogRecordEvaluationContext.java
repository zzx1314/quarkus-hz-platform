package org.huazhi.system.syslog.common.parse;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.huazhi.system.syslog.common.context.LogRecordContext;

/**
 * Quarkus 版方法上下文变量容器
 * 原 Spring 的 MethodBasedEvaluationContext 替代
 * 用于存储方法参数、全局变量、返回值及异常信息
 */
public class LogRecordEvaluationContext {

    private final Map<String, Object> variables = new HashMap<>();

    /**
     * 构造方法
     *
     * @param rootObject 方法所在对象
     * @param method 方法对象
     * @param arguments 方法参数数组
     * @param ret 返回值
     * @param errorMsg 异常信息
     */
    public LogRecordEvaluationContext(Object rootObject, Method method, Object[] arguments,
                                      Object ret, String errorMsg) {

        // 绑定方法参数到变量名 arg0, arg1, ... 或 param0, param1...
        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                variables.put("arg" + i, arguments[i]);
                variables.put("param" + i, arguments[i]);
            }
        }

        // 加入 LogRecordContext 中的线程局部变量
        Map<String, Object> methodVariables = LogRecordContext.getVariables();
        if (methodVariables != null) {
            variables.putAll(methodVariables);
        }

        // 加入全局变量，但不覆盖已有的
        Map<String, Object> globalVariables = LogRecordContext.getGlobalVariableMap();
        if (globalVariables != null) {
            for (Map.Entry<String, Object> entry : globalVariables.entrySet()) {
                variables.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }

        // 特殊变量
        variables.put("_ret", ret);
        variables.put("_errorMsg", errorMsg);

        // rootObject 可选存储
        variables.put("_root", rootObject);
    }

    /**
     * 设置变量
     */
    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    /**
     * 获取变量
     */
    public Object getVariable(String name) {
        return variables.get(name);
    }

    /**
     * 获取全部变量
     */
    public Map<String, Object> getVariables() {
        return variables;
    }
}

