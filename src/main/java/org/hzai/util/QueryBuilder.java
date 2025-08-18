package org.hzai.util;

import java.util.*;

public class QueryBuilder {

    private final StringJoiner query = new StringJoiner(" and ");
    private final Map<String, Object> params = new HashMap<>();
    private String orderByClause = "";
    private String alias = ""; // 新增实体别名

    private QueryBuilder() {}

    public static QueryBuilder create() {
        return new QueryBuilder();
    }

    /** 设置实体别名 */
    public QueryBuilder alias(String alias) {
        this.alias = alias;
        return this;
    }

    /** 拼接字段名 + 参数名 */
    private String getFieldWithAlias(String field) {
        return (alias.isEmpty() ? "" : alias + ".") + field;
    }

    public QueryBuilder like(String field, String value) {
        if (value != null && !value.trim().isEmpty()) {
            query.add(getFieldWithAlias(field) + " like :" + field);
            params.put(field, "%" + value.trim() + "%");
        }
        return this;
    }

    public QueryBuilder equal(String field, Object value) {
        if (value != null) {
            query.add(getFieldWithAlias(field) + " = :" + field);
            params.put(field, value);
        }
        return this;
    }

     public QueryBuilder notEqual(String field, Object value) {
        if (value != null) {
            query.add(getFieldWithAlias(field) + " != :" + field);
            params.put(field, value);
        }
        return this;
    }

    public QueryBuilder in(String field, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            query.add(getFieldWithAlias(field) + " in (:" + field + ")");
            params.put(field, values);
        }
        return this;
    }

    public QueryBuilder between(String field, Comparable<?> start, Comparable<?> end) {
        if (start != null && end != null) {
            String startKey = field + "_start";
            String endKey = field + "_end";
            query.add(getFieldWithAlias(field) + " between :" + startKey + " and :" + endKey);
            params.put(startKey, start);
            params.put(endKey, end);
        }
        return this;
    }

    public QueryBuilder greaterThan(String field, Comparable<?> value) {
        if (value != null) {
            query.add(getFieldWithAlias(field) + " > :" + field + "_gt");
            params.put(field + "_gt", value);
        }
        return this;
    }

    public QueryBuilder lessThan(String field, Comparable<?> value) {
        if (value != null) {
            query.add(getFieldWithAlias(field) + " < :" + field + "_lt");
            params.put(field + "_lt", value);
        }
        return this;
    }

    public QueryBuilder orderBy(String orderByClause) {
        if (orderByClause != null && !orderByClause.trim().isEmpty()) {
            this.orderByClause = " order by " + (alias.isEmpty() ? "" : alias + ".") + orderByClause;
        }
        return this;
    }

    public String getQuery() {
        return query.length() == 0 ? "1=1" + orderByClause : query.toString() + orderByClause;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}