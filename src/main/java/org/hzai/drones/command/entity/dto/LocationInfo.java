package org.hzai.drones.command.entity.dto;

import lombok.Data;

@Data
public class LocationInfo {
    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;
}
