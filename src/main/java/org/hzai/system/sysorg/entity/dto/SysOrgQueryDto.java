package org.hzai.system.sysorg.entity.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Data;

@Data
public class SysOrgQueryDto {

    @QueryParam("name")
    private String name;

    @QueryParam("type")
    private String type;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    private Integer notId;
}
