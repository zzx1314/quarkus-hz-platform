package org.huazhi.system.syslog.common.parser;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.comparison.ComparisonService;
import de.danielbechler.diff.node.DiffNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import org.huazhi.system.syslog.common.context.LogRecordContext;
import org.huazhi.system.syslog.common.diff.ArrayDiffer;
import org.huazhi.system.syslog.common.diff.IDiffItemsToLogContentService;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Quarkus/CDI 版本的 DiffParseFunction
 */
@Slf4j
@ApplicationScoped
public class DiffParseFunction {

    public static final String DIFF_FUNCTION_NAME = "_DIFF";
    public static final String OLD_OBJECT = "_oldObj";

    @Inject
    IDiffItemsToLogContentService diffItemsToLogContentService;

    private final Set<Class<?>> comparisonSet = new HashSet<>();

    public String functionName() {
        return DIFF_FUNCTION_NAME;
    }

    /**
     * 对比两个对象并生成日志内容
     */
    public String diff(Object source, Object target) {
        if (source == null && target == null) {
            return "";
        }

        if (source == null || target == null) {
            try {
                Class<?> clazz = source == null ? target.getClass() : source.getClass();
                source = source == null ? clazz.getDeclaredConstructor().newInstance() : source;
                target = target == null ? clazz.getDeclaredConstructor().newInstance() : target;
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        if (!Objects.equals(source.getClass(), target.getClass())) {
            log.error("diff的两个对象类型不同, source.class={}, target.class={}", source.getClass(), target.getClass());
            return "";
        }

        ObjectDifferBuilder objectDifferBuilder = ObjectDifferBuilder.startBuilding();
        ObjectDifferBuilder register = objectDifferBuilder
                .differs().register((differDispatcher, nodeQueryService) ->
                        new ArrayDiffer(differDispatcher, (ComparisonService) objectDifferBuilder.comparison(), objectDifferBuilder.identity()));

        for (Class<?> clazz : comparisonSet) {
            register.comparison().ofType(clazz).toUseEqualsMethod();
        }

        DiffNode diffNode = register.build().compare(target, source);
        return diffItemsToLogContentService.toLogContent(diffNode, source, target);
    }

    /**
     * 对比方法上下文中的旧对象和传入的新对象
     */
    public String diff(Object newObj) {
        Object oldObj = LogRecordContext.getMethodOrGlobal(OLD_OBJECT);
        return diff(oldObj, newObj);
    }

    public void addUseEqualsClass(List<String> classList) {
        if (classList != null && !classList.isEmpty()) {
            for (String clazzName : classList) {
                try {
                    Class<?> clazz = Class.forName(clazzName);
                    comparisonSet.add(clazz);
                } catch (ClassNotFoundException e) {
                    log.warn("无效的比对类型, className={}", clazzName);
                }
            }
        }
    }

    public void addUseEqualsClass(Class<?> clazz) {
        if (clazz != null) {
            comparisonSet.add(clazz);
        }
    }
}
