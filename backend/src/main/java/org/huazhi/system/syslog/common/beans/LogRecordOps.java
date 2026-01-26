package org.huazhi.system.syslog.common.beans;


public class LogRecordOps {
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private LogRecordOps instance = new LogRecordOps();
        
        public Builder successLogTemplate(String successLogTemplate) {
            instance.successLogTemplate = successLogTemplate;
            return this;
        }
        
        public Builder failLogTemplate(String failLogTemplate) {
            instance.failLogTemplate = failLogTemplate;
            return this;
        }
        
        public Builder operatorId(String operatorId) {
            instance.operatorId = operatorId;
            return this;
        }
        
        public Builder type(String type) {
            instance.type = type;
            return this;
        }
        
        public Builder bizNo(String bizNo) {
            instance.bizNo = bizNo;
            return this;
        }
        
        public Builder subType(String subType) {
            instance.subType = subType;
            return this;
        }
        
        public Builder extra(String extra) {
            instance.extra = extra;
            return this;
        }
        
        public Builder condition(String condition) {
            instance.condition = condition;
            return this;
        }
        
        public Builder isSuccess(String isSuccess) {
            instance.isSuccess = isSuccess;
            return this;
        }
        
        public LogRecordOps build() {
            return instance;
        }
    }
    private String successLogTemplate;
    private String failLogTemplate;
    private String operatorId;
    private String type;
    private String bizNo;
    private String subType;
    private String extra;
    private String condition;
    private String isSuccess;
    public String getSuccessLogTemplate() {
        return successLogTemplate;
    }
    public void setSuccessLogTemplate(String successLogTemplate) {
        this.successLogTemplate = successLogTemplate;
    }
    public String getFailLogTemplate() {
        return failLogTemplate;
    }
    public void setFailLogTemplate(String failLogTemplate) {
        this.failLogTemplate = failLogTemplate;
    }
    public String getOperatorId() {
        return operatorId;
    }
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getBizNo() {
        return bizNo;
    }
    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }
    public String getSubType() {
        return subType;
    }
    public void setSubType(String subType) {
        this.subType = subType;
    }
    public String getExtra() {
        return extra;
    }
    public void setExtra(String extra) {
        this.extra = extra;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public String getIsSuccess() {
        return isSuccess;
    }
    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    
}
