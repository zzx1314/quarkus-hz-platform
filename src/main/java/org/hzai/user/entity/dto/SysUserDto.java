package org.hzai.user.entity.dto;

import org.hzai.user.entity.SysUser;

import jakarta.ws.rs.QueryParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SysUserDto extends SysUser {
    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}
