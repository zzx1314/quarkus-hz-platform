package  org.huazhi.system.syslog.common.diff;

import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.selector.ElementSelector;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.lang.reflect.Field;
import java.util.*;

import org.huazhi.system.syslog.common.annotation.DIffLogIgnore;
import org.huazhi.system.syslog.common.annotation.DiffLogAllFields;
import org.huazhi.system.syslog.common.annotation.DiffLogField;
import org.huazhi.system.syslog.common.configuration.LogRecordProperties;
import org.huazhi.system.syslog.common.service.IFunctionService;

/**
 * Quarkus/CDI 版本的差异日志生成服务
 */
@Slf4j
@Getter
@Setter
@ApplicationScoped
public class DefaultDiffItemsToLogContentService implements IDiffItemsToLogContentService {

    @Inject
    IFunctionService functionService;

    @Inject
    LogRecordProperties logRecordProperties;

    @Override
    public String toLogContent(DiffNode diffNode, final Object sourceObject, final Object targetObject) {
        if (!diffNode.hasChanges()) {
            return "";
        }
        DiffLogAllFields annotation = sourceObject.getClass().getAnnotation(DiffLogAllFields.class);
        StringBuilder stringBuilder = new StringBuilder();
        Set<DiffNode> processedNodes = new HashSet<>();
        diffNode.visit((node, visit) -> generateAllFieldLog(sourceObject, targetObject, stringBuilder, node, annotation, processedNodes));
        processedNodes.clear();
        return stringBuilder.toString().replaceAll(logRecordProperties.getFieldSeparator().concat("$"), "");
    }

    private void generateAllFieldLog(Object sourceObject, Object targetObject, StringBuilder stringBuilder, DiffNode node,
                                     DiffLogAllFields annotation, Set<DiffNode> processedNodes) {

        if (node.isRootNode() || node.getValueTypeInfo() != null || processedNodes.contains(node)) {
            return;
        }

        DIffLogIgnore logIgnore = node.getFieldAnnotation(DIffLogIgnore.class);
        if (logIgnore != null) {
            markNodeProcessed(node, processedNodes);
            return;
        }

        DiffLogField diffLogFieldAnnotation = node.getFieldAnnotation(DiffLogField.class);
        if (annotation == null && diffLogFieldAnnotation == null) {
            return;
        }

        String fieldLogName = getFieldLogName(node, diffLogFieldAnnotation, annotation != null);
        if (fieldLogName == null || fieldLogName.isEmpty()) {
            return;
        }

        boolean valueIsContainer = valueIsContainer(node, sourceObject, targetObject);
        String functionName = diffLogFieldAnnotation != null ? diffLogFieldAnnotation.function() : "";
        String logContent = valueIsContainer
                ? getCollectionDiffLogContent(fieldLogName, node, sourceObject, targetObject, functionName)
                : getDiffLogContent(fieldLogName, node, sourceObject, targetObject, functionName);

        if (logContent != null && !logContent.isEmpty()) {
            stringBuilder.append(logContent).append(logRecordProperties.getFieldSeparator());
        }

        markNodeProcessed(node, processedNodes);
    }

    @SuppressWarnings("unchecked")
    private void markNodeProcessed(DiffNode node, Set<DiffNode> processedNodes) {
        processedNodes.add(node);
        if (node.hasChildren()) {
            try {
                Field childrenField = DiffNode.class.getDeclaredField("children");
                childrenField.setAccessible(true);
                Map<ElementSelector, DiffNode> children = (Map<ElementSelector, DiffNode>) childrenField.get(node);
                if (children != null) {
                    for (DiffNode child : children.values()) {
                        markNodeProcessed(child, processedNodes);
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.warn("无法访问 DiffNode children 字段", e);
            }
        }
    }

    private String getFieldLogName(DiffNode node, DiffLogField diffLogFieldAnnotation, boolean isField) {
        String fieldLogName = diffLogFieldAnnotation != null ? diffLogFieldAnnotation.name() : node.getPropertyName();
        if (node.getParentNode() != null) {
            fieldLogName = getParentFieldName(node, isField) + fieldLogName;
        }
        return fieldLogName;
    }

    private boolean valueIsContainer(DiffNode node, Object sourceObject, Object targetObject) {
        if (sourceObject != null) {
            Object sourceValue = node.canonicalGet(sourceObject);
            if (sourceValue == null) {
                if (targetObject != null) {
                    Object targetValue = node.canonicalGet(targetObject);
                    return targetValue instanceof Collection || (targetValue != null && targetValue.getClass().isArray());
                }
            } else {
                return sourceValue instanceof Collection || sourceValue.getClass().isArray();
            }
        }
        return false;
    }

    private String getParentFieldName(DiffNode node, boolean isField) {
        DiffNode parent = node.getParentNode();
        String fieldNamePrefix = "";
        while (parent != null) {
            DiffLogField diffLogFieldAnnotation = parent.getFieldAnnotation(DiffLogField.class);
            if ((diffLogFieldAnnotation == null && !isField) || parent.isRootNode()) {
                parent = parent.getParentNode();
                continue;
            }
            fieldNamePrefix = (diffLogFieldAnnotation != null
                    ? diffLogFieldAnnotation.name()
                    : parent.getPropertyName()) + logRecordProperties.getOfWord() + fieldNamePrefix;
            parent = parent.getParentNode();
        }
        return fieldNamePrefix;
    }

    private String getCollectionDiffLogContent(String fieldLogName, DiffNode node, Object sourceObject, Object targetObject, String functionName) {
        Collection<Object> sourceList = getListValue(node, sourceObject);
        Collection<Object> targetList = getListValue(node, targetObject);
        Collection<Object> addedItems = listSubtract(targetList, sourceList);
        Collection<Object> deletedItems = listSubtract(sourceList, targetList);
        return logRecordProperties.formatList(fieldLogName, listToContent(functionName, addedItems), listToContent(functionName, deletedItems));
    }

    private String getDiffLogContent(String fieldLogName, DiffNode node, Object sourceObject, Object targetObject, String functionName) {
        switch (node.getState()) {
            case ADDED:
                return logRecordProperties.formatAdd(fieldLogName, getFunctionValue(node.canonicalGet(targetObject), functionName));
            case CHANGED:
                return logRecordProperties.formatUpdate(fieldLogName,
                        getFunctionValue(node.canonicalGet(sourceObject), functionName),
                        getFunctionValue(node.canonicalGet(targetObject), functionName));
            case REMOVED:
                return logRecordProperties.formatDeleted(fieldLogName, getFunctionValue(node.canonicalGet(sourceObject), functionName));
            default:
                log.warn("diff log not support state: {}", node.getState());
                return "";
        }
    }

    private Collection<Object> getListValue(DiffNode node, Object object) {
        Object fieldValue = node.canonicalGet(object);

        if (fieldValue == null) {
            return new ArrayList<>();
        }

        if (fieldValue.getClass().isArray()) {
            return new ArrayList<>(Arrays.asList((Object[]) fieldValue));
        }

        if (fieldValue instanceof Collection<?> collection) {
            return new ArrayList<>(collection);
        }

        return new ArrayList<>();
    }

    private Collection<Object> listSubtract(Collection<Object> minuend, Collection<Object> subtrahend) {
        Collection<Object> result = new ArrayList<>(minuend);
        result.removeAll(subtrahend);
        return result;
    }

    private String listToContent(String functionName, Collection<Object> items) {
        if (items == null || items.isEmpty()) return "";
        StringBuilder content = new StringBuilder();
        for (Object item : items) {
            content.append(getFunctionValue(item, functionName)).append(logRecordProperties.getListItemSeparator());
        }
        return content.toString().replaceAll(logRecordProperties.getListItemSeparator() + "$", "");
    }

    private String getFunctionValue(Object value, String functionName) {
        if (value == null) return "";
        if (functionName == null || functionName.isEmpty()) {
            return value.toString();
        }
        return functionService.apply(functionName, value.toString());
    }
}
