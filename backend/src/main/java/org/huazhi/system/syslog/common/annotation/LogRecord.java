package org.huazhi.system.syslog.common.annotation;
import java.lang.annotation.*;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@InterceptorBinding
public @interface LogRecord {
    /**
     * @return 方法执行成功后的日志模版
     */
    @Nonbinding
    String success() default "";

    /**
     * @return 方法执行失败后的日志模版
     */
    @Nonbinding
    String fail() default "";

    /**
     * @return 日志的操作人
     */
    @Nonbinding
    String operator() default "";

    /**
     * @return 操作日志的类型，比如：订单类型、商品类型
     */
    @Nonbinding
    String type() default "";

    /**
     * @return 日志的子类型，比如订单的C端日志，和订单的B端日志，type都是订单类型，但是子类型不一样
     */
    @Nonbinding
    String subType() default "";

    /**
     * @return 日志绑定的业务标识
     */
    @Nonbinding
    String bizNo() default "";

    /**
     * @return 日志的额外信息
     */
    @Nonbinding
    String extra() default "";

    /**
     * @return 是否记录日志
     */
    @Nonbinding
    String condition() default "";

    /**
     * 记录成功日志的条件
     *
     * @return 表示成功的表达式，默认为空，代表不抛异常为成功
     */
    @Nonbinding
    String successCondition() default "";
}
