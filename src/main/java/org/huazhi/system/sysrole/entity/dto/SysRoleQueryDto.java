package org.huazhi.system.sysrole.entity.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SysRoleQueryDto {

    @QueryParam("id")
    private Long id;

    @QueryParam("name")
    private String name;

    @QueryParam("code")
    private String code;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}
