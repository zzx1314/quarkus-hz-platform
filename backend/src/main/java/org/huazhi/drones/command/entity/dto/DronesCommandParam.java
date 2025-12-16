package org.huazhi.drones.command.entity.dto;

import lombok.Data;

@Data
public class DronesCommandParam {
    private String type;

    private String param;

    private Long taskId;

    private Long deviceId;
}
