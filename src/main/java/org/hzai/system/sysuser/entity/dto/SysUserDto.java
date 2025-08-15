package org.hzai.system.sysuser.entity.dto;


import java.util.List;

import org.hzai.system.sysrole.entity.SysRole;

import io.vertx.core.json.JsonArray;
import lombok.Data;

@Data
public class SysUserDto {
   private Long id;

	private String username;

    private List<SysRole> roles;

    private List<JsonArray> permissions;
}
