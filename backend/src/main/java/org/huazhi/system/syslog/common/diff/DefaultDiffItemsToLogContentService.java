package org.huazhi.system.syslog.common.diff;

import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.selector.ElementSelector;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.lang.reflect.Field;
import java.util.*;

import org.huazhi.system.syslog.common.annotation.DIffLogIgnore;
import org.huazhi.system.syslog.common.annotation.DiffLogAllFields;
import org.huazhi.system.syslog.common.annotation.DiffLogField;
import org.huazhi.system.syslog.common.configuration.LogRecordProperties;
import org.huazhi.system.syslog.common.service.IFunctionService;
import org.jboss.logging.Logger;

/**
 * DefaultDiffItemsToLogContentService
 *
 * 将差异项转换为可日志化的文本内容，供审计、日志记录或调试使用。
 * 负责遍历 DiffNode 树，基于 DiffLog 注解及配置，输出字段级别的日志文本。
 */
@ApplicationScoped
public class DefaultDiffItemsToLogContentService implements IDiffItemsToLogContentService {
    private static final Logger log = Logger.getLogger(DefaultDiffItemsToLogContentService.class);

    @Inject
    IFunctionService functionService;

    @Inject
    LogRecordProperties logRecordProperties;

    /**
     * 将 DiffNode 转换为日志内容字符串。
     *
     * 规则：如果 diffNode 没有变化，返回空字符串；否则遍历节点，基于 DiffLogAllFields 注解/字段注解输出日志。
     *
     * @param diffNode     当前差异节点
     * @param sourceObject 源对象（用于获取字段名/注解信息）
     * @param targetObject 目标对象（用于对比结果）
     * @return 组成的日志内容字符串
     */
    @Override
    public String toLogContent(DiffNode diffNode, final Object sourceObject, final Object targetObject) {
        if (!diffNode.hasChanges()) {
            return "";
        }
        DiffLogAllFields annotation = sourceObject.getClass().getAnnotation(DiffLogAllFields.class);
        StringBuilder stringBuilder = new StringBuilder();
        Set<DiffNode> processedNodes = new HashSet<>();
        diffNode.visit((node, visit) -> generateAllFieldLog(sourceObject, targetObject, stringBuilder, node, annotation,
                processedNodes));
        processedNodes.clear();
        return stringBuilder.toString().replaceAll(logRecordProperties.getFieldSeparator().concat("$"), "");
    }

    /**
     * 递归生成所有字段的日志信息。
     * 通过检查字段注解、DiffLogAllFields 注解以及 DIffLogIgnore 等元数据，决定哪些字段需要输出日志，以及日志的名称和格式。
     */
    private void generateAllFieldLog(Object sourceObject, Object targetObject, StringBuilder stringBuilder,
            DiffNode node,
            DiffLogAllFields annotation, Set<DiffNode> processedNodes) {

        if (node.isRootNode() || node.getValueTypeInfo() != null || processedNodes.contains(node)) {
            return;
        }

        DIffLogIgnore logIgnore = node.getFieldAnnotation(DIffLogIgnore.class);
        if (logIgnore != null) {
            markNodeProcessed(node, processedNodes);
            return;
        }

        @SuppressWarnings("unused")
        DiffLogField diffLogFieldAnnotation = node.getFieldAnnotation(DiffLogField.class);
        if (annotation == null && diffLogFieldAnnotation == null) {
            return;
        }

        String fieldLogName;
        String functionName;

        if (diffLogFieldAnnotation != null) {
            fieldLogName = getFieldLogName(node, diffLogFieldAnnotation, annotation != null);
            functionName = diffLogFieldAnnotation.function();
        } else {
            fieldLogName = node.getPropertyName();
            if (node.getParentNode() != null) {
                fieldLogName = getParentFieldName(node, false) + fieldLogName;
            }
            functionName = "";
        }

        if (fieldLogName == null || fieldLogName.isEmpty()) {
            return;
        }

        boolean valueIsContainer = valueIsContainer(node, sourceObject, targetObject);
        String logContent = valueIsContainer
                ? getCollectionDiffLogContent(fieldLogName, node, sourceObject, targetObject, functionName)
                : getDiffLogContent(fieldLogName, node, sourceObject, targetObject, functionName);

        if (logContent != null && !logContent.isEmpty()) {
            stringBuilder.append(logContent).append(logRecordProperties.getFieldSeparator());
        }

        markNodeProcessed(node, processedNodes);
    }

    @SuppressWarnings("unchecked")
    /**
     * 标记节点及其子节点为已处理，避免重复输出，递归访问 DiffNode 的子节点。
     */
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

    /**
     * 获取当前字段在日志中的名称，若存在 DiffLogField 注解则使用注解的 name，若在集合/父级节点中则拼接前缀。
     */
    private String getFieldLogName(DiffNode node, DiffLogField diffLogFieldAnnotation, boolean isField) {
        String fieldLogName = diffLogFieldAnnotation != null ? diffLogFieldAnnotation.name() : node.getPropertyName();
        if (node.getParentNode() != null) {
            fieldLogName = getParentFieldName(node, isField) + fieldLogName;
        }
        return fieldLogName;
    }

    /**
     * 判断当前节点的值是否为容器类型（集合或数组），用于决定是否走集合日志输出分支。
     */
    private boolean valueIsContainer(DiffNode node, Object sourceObject, Object targetObject) {
        if (sourceObject != null) {
            Object sourceValue = node.canonicalGet(sourceObject);
            if (sourceValue == null) {
                if (targetObject != null) {
                    Object targetValue = node.canonicalGet(targetObject);
                    return targetValue instanceof Collection
                            || (targetValue != null && targetValue.getClass().isArray());
                }
            } else {
                return sourceValue instanceof Collection || sourceValue.getClass().isArray();
            }
        }
        return false;
    }

    /**
     * 构建父级字段名前缀，用于在日志中输出完整的字段路径。
     */
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

    /**
     * 处理集合字段的日志输出，包括增量/删除项的日志内容构造。
     */
    private String getCollectionDiffLogContent(String fieldLogName, DiffNode node, Object sourceObject,
            Object targetObject, String functionName) {
        Collection<Object> sourceList = getListValue(node, sourceObject);
        Collection<Object> targetList = getListValue(node, targetObject);
        Collection<Object> addedItems = listSubtract(targetList, sourceList);
        Collection<Object> deletedItems = listSubtract(sourceList, targetList);
        return logRecordProperties.formatList(fieldLogName, listToContent(functionName, addedItems),
                listToContent(functionName, deletedItems));
    }

    /**
     * 根据 DiffNode 的状态输出对应的日志内容（已添加/修改/删除）。
     */
    private String getDiffLogContent(String fieldLogName, DiffNode node, Object sourceObject, Object targetObject,
            String functionName) {
        switch (node.getState()) {
            case ADDED:
                return logRecordProperties.formatAdd(fieldLogName,
                        getFunctionValue(node.canonicalGet(targetObject), functionName));
            case CHANGED:
                return logRecordProperties.formatUpdate(fieldLogName,
                        getFunctionValue(node.canonicalGet(sourceObject), functionName),
                        getFunctionValue(node.canonicalGet(targetObject), functionName));
            case REMOVED:
                return logRecordProperties.formatDeleted(fieldLogName,
                        getFunctionValue(node.canonicalGet(sourceObject), functionName));
            default:
                log.warnf("diff log not support state: %s", node.getState());
                return "";
        }
    }

    /**
     * 获取 DiffNode 对应字段的值，并将其包装为一个集合，方便后续集合日志输出。
     */
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

    /**
     * 从一个集合中减去另一个集合，返回结果集合。
     */
    private Collection<Object> listSubtract(Collection<Object> minuend, Collection<Object> subtrahend) {
        Collection<Object> result = new ArrayList<>(minuend);
        result.removeAll(subtrahend);
        return result;
    }

    /**
     * 将集合中的项格式化为日志内容字符串，用于输出日志列表。
     */
    private String listToContent(String functionName, Collection<Object> items) {
        if (items == null || items.isEmpty())
            return "";
        StringBuilder content = new StringBuilder();
        for (Object item : items) {
            content.append(getFunctionValue(item, functionName)).append(logRecordProperties.getListItemSeparator());
        }
        return content.toString().replaceAll(logRecordProperties.getListItemSeparator() + "$", "");
    }

    /**
     * 根据 functionName 对值进行加工处理，若未提供 functionName，直接返回字符串形式。
     */
    private String getFunctionValue(Object value, String functionName) {
        if (value == null)
            return "";
        if (functionName == null || functionName.isEmpty()) {
            return value.toString();
        }
        return functionService.apply(functionName, value.toString());
    }
}
