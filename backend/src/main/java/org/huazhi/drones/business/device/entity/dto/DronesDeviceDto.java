package org.huazhi.drones.business.device.entity.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
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
}