package org.hzai.system.sysuser.entity.dto;


import java.util.List;

import lombok.Data;

@Data
public class SysUserDto {
   private Long id;

	private String username;

    private List<Long> roleIdList;

    private List<String> permissions;
}
