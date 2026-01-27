package org.huazhi.system.syslog.common.parser;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.comparison.ComparisonService;
import de.danielbechler.diff.node.DiffNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.huazhi.system.syslog.common.context.LogRecordContext;
import org.huazhi.system.syslog.common.diff.ArrayDiffer;
import org.huazhi.system.syslog.common.diff.IDiffItemsToLogContentService;
import org.jboss.logging.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Quarkus/CDI 版本的 DiffParseFunction
 */
@ApplicationScoped
public class DiffParseFunction {
    private static final Logger log = Logger.getLogger(DiffParseFunction.class);

    public static final String DIFF_FUNCTION_NAME = "_DIFF";
    public static final String OLD_OBJECT = "_oldObj";

    @Inject
    IDiffItemsToLogContentService diffItemsToLogContentService;

    private final Set<Class<?>> comparisonSet = new HashSet<>();

    public String functionName() {
        return DIFF_FUNCTION_NAME;
    }

    public String diff(Object source, Object target) {
        if (source == null && target == null) {
            return "";
        }

        if (source == null || target == null) {
            return diffWithEmpty(source, target);
        }

        return doDiff(source, target);
    }

    private String diffWithEmpty(Object source, Object target) {
        Object nonNull = source != null ? source : target;
        Object empty = newEmptyInstance(nonNull.getClass());

        return source == null
                ? doDiff(empty, target)
                : doDiff(source, empty);
    }

    private Object newEmptyInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String doDiff(Object source, Object target) {
        if (!source.getClass().equals(target.getClass())) {
            log.errorf("diff的两个对象类型不同, source.class=%s, target.class=%s",
                    source.getClass(), target.getClass());
            return "";
        }

        ObjectDifferBuilder builder = ObjectDifferBuilder.startBuilding();
        ObjectDifferBuilder register = builder.differs().register(
                (differDispatcher, nodeQueryService) -> new ArrayDiffer(differDispatcher,
                        (ComparisonService) builder.comparison(),
                        builder.identity()));

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
                    log.warnf("无效的比对类型, className=%s", clazzName);
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
