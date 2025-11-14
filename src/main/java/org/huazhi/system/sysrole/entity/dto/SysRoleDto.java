package org.huazhi.system.sysrole.entity.dto;

import lombok.Data;

@Data
public class SysRoleDto {
    private Long id;

	/**
	 * 角色名
	 */
	private String name;

	/**
	 * 角色编码
	 */
	private String code;

	/**
	 * 角色描述
	 */
	private String description;
}
