package org.huazhi.system.sysorg.entity.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SysOrgQueryDto {

    @QueryParam("name")
    private String name;

    @QueryParam("type")
    private String type;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    private Long notId;
}
