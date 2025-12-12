package org.huazhi.drones.command.entity.webscoketdto.action;

import org.huazhi.drones.command.entity.webscoketdto.DronesRoute;

import lombok.Data;

@Data
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
}
