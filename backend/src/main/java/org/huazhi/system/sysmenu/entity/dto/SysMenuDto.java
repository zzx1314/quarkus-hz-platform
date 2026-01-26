package org.huazhi.system.sysmenu.entity.dto;


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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPathUrl() {
		return pathUrl;
	}

	public void setPathUrl(String pathUrl) {
		this.pathUrl = pathUrl;
	}

	
}
