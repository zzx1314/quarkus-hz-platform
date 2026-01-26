package org.huazhi.drones.business.command.entity.dto;

import java.util.List;


public class DroneTaskPlan {
    /**
     * 任务编号
     */
    private String taskId;

    /**
     * 任务动作
     */
    private List<DronesAction> actionArray;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<DronesAction> getActionArray() {
        return actionArray;
    }

    public void setActionArray(List<DronesAction> actionArray) {
        this.actionArray = actionArray;
    }

    
}
