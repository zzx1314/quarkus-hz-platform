package org.hzai.system.sysuser.entity.dto;

import lombok.Data;

import java.util.List;

import org.hzai.system.sysuser.entity.SysUser;

@Data
public class UserInfo{

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

}
