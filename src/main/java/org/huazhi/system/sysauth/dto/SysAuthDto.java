package org.huazhi.system.sysauth.dto;

import lombok.Data;

import java.util.List;

@Data
public class SysAuthDto {

	/**
	 * 角色id
	 */
	private String roleCode;

	/**
	 * 角色权限
	 */
	private List<Integer> authList;

}
