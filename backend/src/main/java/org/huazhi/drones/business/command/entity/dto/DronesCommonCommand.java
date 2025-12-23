package org.huazhi.drones.business.command.entity.dto;

import lombok.Data;

@Data
public class DronesCommonCommand {
    private Long commandId;

    private Long taskId;

    private String deviceId;

    private String type;

    private Object params;
}
