package org.hzai.system.sysdict.entity.dto;

import lombok.Data;
import jakarta.ws.rs.QueryParam;

@Data
public class SysDictQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}