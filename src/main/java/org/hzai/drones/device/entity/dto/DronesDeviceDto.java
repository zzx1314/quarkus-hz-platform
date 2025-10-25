package org.hzai.drones.device.entity.dto;

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
     * 设备序列号
     */
    private String serialNumber;

    /**
     * 设备状态
     */
    private String status;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 设备位置
     */
    private String location;

    /**
     * 设备制造商
     */
    private String manufacturer;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 固件版本
     */
    private String firmwareVersion;

    /**
     * 电池状态
     */
    private String batteryStatus;

    /**
     * 最后维护日期
     */
    private String lastMaintenanceDate;

    /**
     * 通信时间
     */
    private LocalDateTime commTime;
}