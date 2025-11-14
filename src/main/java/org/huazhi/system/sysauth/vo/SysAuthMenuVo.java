package org.huazhi.system.sysauth.vo;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class SysAuthMenuVo {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 是否全选
	 */
	private Boolean isCheckAll;

	/**
	 * 权限列表
	 */
	private List<SysAuthTitleVo> authList;

	/**
	 * 已使用的权限
	 */
	private Set<Integer> useAuthList;

}
