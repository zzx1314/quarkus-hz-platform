package org.huazhi.drones.device.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
public class DronesDeviceQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    @QueryParam("deviceId")
    private String deviceId;

    /**
     * 设备类型
     */
    @QueryParam("deviceType")
    private String deviceType;
    /**
     * 设备状态
     */
    @QueryParam("status")
    private String status;
}