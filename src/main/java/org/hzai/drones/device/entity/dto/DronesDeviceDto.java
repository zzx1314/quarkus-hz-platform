package org.hzai.drones.device.entity.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DronesDeviceDto {
    private Long id;

    private String deviceId;

    private LocalDateTime commTime;
    
    private String status;
}