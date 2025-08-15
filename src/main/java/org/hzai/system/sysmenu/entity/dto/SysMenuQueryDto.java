package org.hzai.system.sysmenu.entity.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Data;

@Data
public class SysMenuQueryDto {
    @QueryParam("name")
    private String name;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

}
