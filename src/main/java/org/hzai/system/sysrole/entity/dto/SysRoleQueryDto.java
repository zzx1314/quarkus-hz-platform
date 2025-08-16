package org.hzai.system.sysrole.entity.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Data;

@Data
public class SysRoleQueryDto {

    @QueryParam("id")
    private Long id;

    @QueryParam("name")
    private String name;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}
