package org.hzai.system.org.entity.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Data;

@Data
public class SysOrgQueryDto {

    @QueryParam("name")
    private String name;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}
