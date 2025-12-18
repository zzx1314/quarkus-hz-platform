package org.huazhi.drones.command.entity.dto;

import lombok.Data;

@Data
public class DronesCommonCommand {
    private Long commandId;

    private String deviceId;

    private String type;

    private Object params;
}
