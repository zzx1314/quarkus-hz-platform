package org.huazhi.system.syslog.common.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class LogRecord {
    /**
     * id
     */
    private Serializable id;
    /**
     * 租户
     */
    private String tenant;

    /**
     * 保存的操作日志的类型，比如：订单类型、商品类型
     *
     * @since 2.0.0 从 prefix 修改为了type
     */
    private String type;
    /**
     * 日志的子类型，比如订单的C端日志，和订单的B端日志，type都是订单类型，但是子类型不一样
     * @since 2.0.0 从 category 修改为 subtype
     */
    private String subType;

    /**
     * 日志绑定的业务标识
     */
    private String bizNo;
    /**
     * 操作人
     */
    private String operator;

    /**
     * 日志内容
     */
    private String action;
    /**
     * 记录是否是操作失败的日志
     */
    private boolean fail;
    /**
     * 日志的创建时间
     */
    private Date createTime;
    /**
     * 日志的额外信息
     *
     * @since 2.0.0 从detail 修改为extra
     */
    private String extra;


    /**
     * IP地址
     */
    private String ip;

    /**
     * 打印日志的代码信息
     * CodeVariableType 日志记录的ClassName、MethodName
     */
    private Map<CodeVariableType, Object> codeVariable;
    
    public LogRecord() {
    }
    
    public LogRecord(Serializable id, String tenant, String type, String subType, String bizNo, String operator, String action, boolean fail, Date createTime, String extra, String ip, Map<CodeVariableType, Object> codeVariable) {
        this.id = id;
        this.tenant = tenant;
        this.type = type;
        this.subType = subType;
        this.bizNo = bizNo;
        this.operator = operator;
        this.action = action;
        this.fail = fail;
        this.createTime = createTime;
        this.extra = extra;
        this.ip = ip;
        this.codeVariable = codeVariable;
    }
    
    public static LogRecordBuilder builder() {
        return new LogRecordBuilder();
    }

    public Serializable getId() {
        return id;
    }

    public void setId(Serializable id) {
        this.id = id;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isFail() {
        return fail;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<CodeVariableType, Object> getCodeVariable() {
        return codeVariable;
    }

    public void setCodeVariable(Map<CodeVariableType, Object> codeVariable) {
        this.codeVariable = codeVariable;
    }

    @Override
    public String toString() {
        return "LogRecord{" +
                "id=" + id +
                ", tenant='" + tenant + '\'' +
                ", type='" + type + '\'' +
                ", subType='" + subType + '\'' +
                ", bizNo='" + bizNo + '\'' +
                ", operator='" + operator + '\'' +
                ", action='" + action + '\'' +
                ", fail=" + fail +
                ", createTime=" + createTime +
                ", extra='" + extra + '\'' +
                ", ip='" + ip + '\'' +
                ", codeVariable=" + codeVariable +
                '}';
    }

    public static class LogRecordBuilder {
        private Serializable id;
        private String tenant;
        private String type;
        private String subType;
        private String bizNo;
        private String operator;
        private String action;
        private boolean fail;
        private Date createTime;
        private String extra;
        private String ip;
        private Map<CodeVariableType, Object> codeVariable;

        public LogRecordBuilder() {
        }

        public LogRecordBuilder id(Serializable id) {
            this.id = id;
            return this;
        }

        public LogRecordBuilder tenant(String tenant) {
            this.tenant = tenant;
            return this;
        }

        public LogRecordBuilder type(String type) {
            this.type = type;
            return this;
        }

        public LogRecordBuilder subType(String subType) {
            this.subType = subType;
            return this;
        }

        public LogRecordBuilder bizNo(String bizNo) {
            this.bizNo = bizNo;
            return this;
        }

        public LogRecordBuilder operator(String operator) {
            this.operator = operator;
            return this;
        }

        public LogRecordBuilder action(String action) {
            this.action = action;
            return this;
        }

        public LogRecordBuilder fail(boolean fail) {
            this.fail = fail;
            return this;
        }

        public LogRecordBuilder createTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        public LogRecordBuilder extra(String extra) {
            this.extra = extra;
            return this;
        }

        public LogRecordBuilder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public LogRecordBuilder codeVariable(Map<CodeVariableType, Object> codeVariable) {
            this.codeVariable = codeVariable;
            return this;
        }

        public LogRecord build() {
            return new LogRecord(id, tenant, type, subType, bizNo, operator, action, fail, createTime, extra, ip, codeVariable);
        }
    }
}