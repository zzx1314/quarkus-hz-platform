package org.hzai.system.sysdictitem.entity.dto;

import lombok.Data;
import jakarta.ws.rs.QueryParam;

@Data
public class SysDictItemQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    @QueryParam("dicType")
    private String dicType;
}