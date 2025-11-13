package org.hzai.drones.config.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true) 
public class DronesConfigQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    @QueryParam("ids")
    private List<Long> ids;
}