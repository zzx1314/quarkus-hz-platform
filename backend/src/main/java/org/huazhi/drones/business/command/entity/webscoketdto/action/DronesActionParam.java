package org.huazhi.drones.business.command.entity.webscoketdto.action;

import org.huazhi.drones.business.command.entity.webscoketdto.route.DronesRoute;


public class  DronesActionParam {
    /**
     * 悬停时间
     */
    private Integer timeSec;
    /**
     * 目标高度
     */
    private Double targetAlt;

    /**
     * 事件
     */
    private DronesActionParamEvent event;

    /**
     * 目标航点
     */
    private DronesRoute targetWp;

    /**
     * 循环次数
     */
    private Integer num;

    /**
     * 循环间隔
     */
    private Integer intervalSec;

    /**
     * service -type
     */
    private String type;

    /**
     * 失败原因
     */
    private String reason;

    // yolo 相关参数
    private String model;

    private String input;


    // deepsort 相关参数
    private Integer max_age;


    // RTSP 相关参数
    private String url;

    private String bitrate;

    public Integer getTimeSec() {
        return timeSec;
    }

    public void setTimeSec(Integer timeSec) {
        this.timeSec = timeSec;
    }

    public Double getTargetAlt() {
        return targetAlt;
    }

    public void setTargetAlt(Double targetAlt) {
        this.targetAlt = targetAlt;
    }

    public DronesActionParamEvent getEvent() {
        return event;
    }

    public void setEvent(DronesActionParamEvent event) {
        this.event = event;
    }

    public DronesRoute getTargetWp() {
        return targetWp;
    }

    public void setTargetWp(DronesRoute targetWp) {
        this.targetWp = targetWp;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getIntervalSec() {
        return intervalSec;
    }

    public void setIntervalSec(Integer intervalSec) {
        this.intervalSec = intervalSec;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Integer getMax_age() {
        return max_age;
    }

    public void setMax_age(Integer max_age) {
        this.max_age = max_age;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }
    
}
