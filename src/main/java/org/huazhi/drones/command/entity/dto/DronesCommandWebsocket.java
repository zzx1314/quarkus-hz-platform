package org.huazhi.drones.command.entity.dto;

import java.util.List;

import lombok.Data;

@Data
public class DronesCommandWebsocket {
    /**
     * 命令id
     */
    Long commandId;

    /**
     * 设备id
     */
    String deviceId;

    /**
     * 命令类型
     */
    String type;

    /**
     * 动作触发器
     */
    List<DronesActionTrigger> actionTriggerArray;

    /**
     * 任务计划
     */
    List<DroneTaskPlan> taskplanArray;
}
