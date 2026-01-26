package org.huazhi.drones.business.command.entity.dto;

import com.fasterxml.jackson.databind.JsonNode;


/**
 * 指令返回
 */
public class DronesCommandReturn {

    private Long commandId;

    private String type;

    private JsonNode params;

    private String status;

    public Long getCommandId() {
        return commandId;
    }

    public void setCommandId(Long commandId) {
        this.commandId = commandId;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
