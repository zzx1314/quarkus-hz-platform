package org.huazhi.system.sysuser.entity.dto;

import java.util.List;

import org.huazhi.system.sysuser.entity.SysUser;


public class SysUserDto extends SysUser {
    private Long id;

    private String username;

    private List<Long> roleIdList;

    private List<String> permissions;

    /**
	 * 角色ID
	 */
	private Long role;

	/**
	 * 组织机构ids
	 */
	private List<Long> orgIds;

    /**
	 * 新密码
	 */
	private String newpassword;

	/**
	 * 确认密码
	 */
	private String newpassword1;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Long> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public Long getRole() {
		return role;
	}

	public void setRole(Long role) {
		this.role = role;
	}

	public List<Long> getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List<Long> orgIds) {
		this.orgIds = orgIds;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public String getNewpassword1() {
		return newpassword1;
	}

	public void setNewpassword1(String newpassword1) {
		this.newpassword1 = newpassword1;
	}
}
