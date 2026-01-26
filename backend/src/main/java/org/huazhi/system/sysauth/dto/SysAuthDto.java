package org.huazhi.system.sysauth.dto;


import java.util.List;


public class SysAuthDto {

	/**
	 * 角色id
	 */
	private String roleCode;

	/**
	 * 角色权限
	 */
	private List<Long> authList;

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public List<Long> getAuthList() {
		return authList;
	}

	public void setAuthList(List<Long> authList) {
		this.authList = authList;
	}

}
