package org.huazhi.drones.business.command.entity.dto;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

/**
 * 指令返回
 */
@Data
public class DronesCommandReturn {

    private Long commandId;

    private String type;

    private JsonNode params;

    private String status;
}
