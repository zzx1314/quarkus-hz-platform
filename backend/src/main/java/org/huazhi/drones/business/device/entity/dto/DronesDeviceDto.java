package org.huazhi.drones.business.device.entity.dto;

import java.time.LocalDateTime;


public class DronesDeviceDto {
    private Long id;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备状态
     */
    private String status;

    /**
     * IP地址
     */
    private String deviceIp;

    /**
     * 速度
     */
    private String speed;

    /**
     * 高度
     */
    private String height;

    /**
     * 剩余电量
     */
    private String battery;

    /**
     * 航向
     */
    private String course;

    /**
     * 设备位置
     */
    private String location;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 通信时间
     */
    private LocalDateTime commTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDateTime getCommTime() {
        return commTime;
    }

    public void setCommTime(LocalDateTime commTime) {
        this.commTime = commTime;
    }
    
}