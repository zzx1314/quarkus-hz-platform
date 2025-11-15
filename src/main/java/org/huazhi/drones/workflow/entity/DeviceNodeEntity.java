package org.huazhi.drones.workflow.entity;

import java.util.List;

import lombok.Data;

@Data
public class DeviceNodeEntity {
    private long deviceId;

    private List<Long> configIds;

    private Long modelId;

    private long routeId;

    private List<String> path;
}
