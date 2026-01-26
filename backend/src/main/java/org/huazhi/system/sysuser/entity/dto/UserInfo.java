package org.huazhi.system.sysuser.entity.dto;


import java.util.List;

import org.huazhi.system.sysuser.entity.SysUser;

public class UserInfo {

	/**
	 * 用户基本信息
	 */
	private SysUser sysUser;

	/**
	 * 权限标识集合
	 */
	private List<String> permissions;

	/**
	 * 角色集合
	 */
	private List<Long> roles;

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public List<Long> getRoles() {
		return roles;
	}

	public void setRoles(List<Long> roles) {
		this.roles = roles;
	}


	

}
