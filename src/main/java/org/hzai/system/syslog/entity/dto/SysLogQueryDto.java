package org.hzai.system.syslog.entity.dto;

import lombok.Data;
import jakarta.ws.rs.QueryParam;

@Data
public class SysLogQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}