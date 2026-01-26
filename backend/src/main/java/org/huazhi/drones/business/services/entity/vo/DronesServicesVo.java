package org.huazhi.drones.business.services.entity.vo;

import com.fasterxml.jackson.databind.JsonNode;


public class DronesServicesVo {
    /**
     * 类型
     */
    private String type;

    /**
     * 参数
     */
    private JsonNode params;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonNode getParams() {
        return params;
    }

    public void setParams(JsonNode params) {
        this.params = params;
    }

    
}
