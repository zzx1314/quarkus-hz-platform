package org.huazhi.drones.business.task.entity.dto;

import jakarta.ws.rs.QueryParam;


public class DronesTaskQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    /**
     * 任务名称
     */
    @QueryParam("taskName")
    private String taskName;

    /**
     * 任务描述
     */
    @QueryParam("taskDescription")
    private String taskDescription;

    /**
     * 任务状态
     */
    @QueryParam("taskStatus")
    private String taskStatus;


    /**
     * 设备ID
     */
    @QueryParam("deviceIdString")
    private String deviceIdString;

    /**
     * 航线名称
     */
    @QueryParam("routeName")
    private String routeName;


    /**
     * 航线ID
     */
    @QueryParam("routeId")
    private Long routeId;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getBeginTime() {
        return beginTime;
    }


    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }


    public String getEndTime() {
        return endTime;
    }


    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getTaskName() {
        return taskName;
    }


    public void setTaskName(String taskName) {
        this.taskName = taskName;
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


    public String getDeviceIdString() {
        return deviceIdString;
    }


    public void setDeviceIdString(String deviceIdString) {
        this.deviceIdString = deviceIdString;
    }


    public String getRouteName() {
        return routeName;
    }


    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }


    public Long getRouteId() {
        return routeId;
    }


    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

}