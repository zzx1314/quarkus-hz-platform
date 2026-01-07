package org.huazhi.drones.business.command.entity.dto;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class DronesCommonCommand {
    private Long commandId;

    private Long taskId;

    private String deviceId;

    private String type;

    private JsonNode params;
}
