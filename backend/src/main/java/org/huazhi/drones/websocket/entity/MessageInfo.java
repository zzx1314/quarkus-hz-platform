package org.huazhi.drones.websocket.entity;

import org.jose4j.json.internal.json_simple.JSONObject;


public class MessageInfo {
    private Long id;

    /**
     * 设备ID
     */
    private String deviceId;

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
     * 位置
     */
    private String location;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息内容
     */
    private Object msg;

    /**
     * 下发指令的参数
     */
    private JSONObject data;

    /**
     * 执行结果
     */
    private String status;

    /**
     * 执行结果返回值
     */
    private String returnValue;

    public MessageInfo() {
    }

    public MessageInfo(String type, Object msg) {
        this.type = type;
        this.msg = msg;
    }

    public MessageInfo(String type, Object msg, JSONObject data) {
        this.type = type;
        this.msg = msg;
        this.data = data;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }
}
