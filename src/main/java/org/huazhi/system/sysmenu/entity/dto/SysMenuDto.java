package org.huazhi.system.sysmenu.entity.dto;

import lombok.Data;

@Data
public class SysMenuDto {
	private Long id;
    /**
	 * 名称
	 */
	private String name;

	/**
	 * 编码
	 */
	private String code;

	/**
	 * 菜单权限标识
	 */
	private String permission;

	/**
	 * 菜单路径
	 */
	private String pathUrl;
}
