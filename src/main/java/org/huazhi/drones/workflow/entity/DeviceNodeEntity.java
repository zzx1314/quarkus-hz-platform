package org.huazhi.drones.workflow.entity;

import java.util.List;

import org.huazhi.drones.command.entity.dto.DronesAction;

import lombok.Data;

@Data
public class DeviceNodeEntity {
    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 配置id
     */
    private List<Long> configIds;

    /**
     * 模型id
     */
    private Long modelId;

    /**
     * 动作集合
     */
    private List<DronesAction> actions; 
}
