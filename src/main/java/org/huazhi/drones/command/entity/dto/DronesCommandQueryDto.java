package org.huazhi.drones.command.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true) 
public class DronesCommandQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    @QueryParam("commandName")
    private String commandName;

    @QueryParam("deviceId")
    private String deviceId;

}