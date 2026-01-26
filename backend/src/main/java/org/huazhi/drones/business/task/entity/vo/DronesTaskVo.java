package org.huazhi.drones.business.task.entity.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;


public class DronesTaskVo {
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 设备ID
     */
    private Long deviceId;


    /**
     * 设备ID字符串
     */
    private String deviceIdString;

    /**
     * 任务描述
     */
    private String taskDescription;

    /**
     * 任务状态
     */
    private String taskStatus;

    /*
     * 工作流UUID
     */
    private String workflowUuid;

    /**
     * 路线ID
     */
    private Long routeId;


    /**
     * 航线名称
     */
    private String routeName;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;


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


    public String getDeviceIdString() {
        return deviceIdString;
    }


    public void setDeviceIdString(String deviceIdString) {
        this.deviceIdString = deviceIdString;
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


    public String getWorkflowUuid() {
        return workflowUuid;
    }


    public void setWorkflowUuid(String workflowUuid) {
        this.workflowUuid = workflowUuid;
    }


    public Long getRouteId() {
        return routeId;
    }


    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }


    public String getRouteName() {
        return routeName;
    }


    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    
}
