package org.huazhi.drones.command.entity.dto;

import java.util.List;

import lombok.Data;

@Data
public class DroneTaskPlan {
    /**
     * 任务编号
     */
    private String taskId;

    /**
     * 任务动作
     */
    private List<DronesAction> actionArray;
}
