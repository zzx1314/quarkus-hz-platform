package org.hzai.drones.device.entity.dto;

import lombok.Data;

/**
 *  无人机上报状态数据
 */
@Data
public class DronesDeviceStatusDto {
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

}
