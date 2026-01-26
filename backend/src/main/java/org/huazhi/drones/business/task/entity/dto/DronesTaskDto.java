package org.huazhi.drones.business.task.entity.dto;


public class DronesTaskDto {
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 设备Id
     */
    private Long deviceId;

    /**
     * 航线Id
     */
    private Long routeId;

    /**
     * 任务描述
     */
    private String taskDescription;

    /**
     * 任务状态
     */
    private String taskStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

}