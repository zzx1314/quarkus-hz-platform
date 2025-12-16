package org.huazhi.drones.command.entity.dto;

import java.util.List;

import lombok.Data;

@Data
public class DronesCommandParam {
    private List<String> type;

    private String param;

    private Long taskId;

    private Long deviceId;
}
