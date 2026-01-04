package org.huazhi.system.syslog.common.parse;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quarkus 版表达式解析器（替代 Spring CachedExpressionEvaluator）
 */
public class LogRecordExpressionEvaluator {

    /**
     * 方法缓存：避免重复反射
     */
    private final Map<MethodKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    /**
     * 表达式缓存：可以缓存编译好的 Qute / MVEL / JEXL 表达式对象
     */
    private final Map<String, Object> expressionCache = new ConcurrentHashMap<>(64);

    /**
     * 解析表达式
     *
     * @param expressionStr 表达式字符串
     * @param methodKey     方法缓存 key
     * @param evalContext   变量上下文
     * @return 解析结果
     */
    public Object parseExpression(String expressionStr, MethodKey methodKey, LogRecordEvaluationContext evalContext) {
        // 从缓存中获取编译好的表达式
        Object expr = expressionCache.computeIfAbsent(expressionStr, key -> compileExpression(key));

        // 执行表达式，返回结果
        return evaluateExpression(expr, evalContext);
    }

    /**
     * 创建 EvaluationContext（方法参数、全局变量、返回值、错误信息）
     */
    public LogRecordEvaluationContext createEvaluationContext(Method method, Object[] args,
                                                              Class<?> targetClass, Object result, String errorMsg) {
        Method targetMethod = getTargetMethod(targetClass, method);
        return new LogRecordEvaluationContext(null, targetMethod, args, result, errorMsg);
    }

    /**
     * 获取目标方法，支持子类重写方法
     */
    private Method getTargetMethod(Class<?> targetClass, Method method) {
        MethodKey key = new MethodKey(targetClass, method);
        return targetMethodCache.computeIfAbsent(key, k -> getMostSpecificMethod(method, targetClass));
    }

    /**
     * 获取最具体的方法（类似 Spring AopUtils.getMostSpecificMethod）
     */
    private Method getMostSpecificMethod(Method method, Class<?> targetClass) {
        try {
            return targetClass.getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            // fallback 返回原方法
            return method;
        }
    }

    /**
     * 编译表达式（可集成 Qute / MVEL / JEXL）
     */
    private Object compileExpression(String expressionStr) {
        // TODO: 根据实际需求选择表达式引擎
        return expressionStr;
    }

    /**
     * 执行表达式
     */
    private Object evaluateExpression(Object expr, LogRecordEvaluationContext context) {
        // TODO: 根据实际需求选择表达式引擎
        // 如果用 Qute，可以渲染模板
        // 暂时直接返回原字符串
        return expr;
    }


    public MethodKey createMethodKey(Class<?> targetClass, Method method) {
        return new MethodKey(targetClass, method);
    }

    /**
     * 方法缓存 Key
     */
    private static class MethodKey {
        private final Class<?> targetClass;
        private final Method method;

        public MethodKey(Class<?> targetClass, Method method) {
            this.targetClass = targetClass;
            this.method = method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MethodKey)) return false;
            MethodKey that = (MethodKey) o;
            return targetClass.equals(that.targetClass) && method.equals(that.method);
        }

        @Override
        public int hashCode() {
            return 31 * targetClass.hashCode() + method.hashCode();
        }
    }
}
