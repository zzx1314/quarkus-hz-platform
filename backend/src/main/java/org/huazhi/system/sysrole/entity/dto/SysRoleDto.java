package org.huazhi.system.sysrole.entity.dto;


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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
