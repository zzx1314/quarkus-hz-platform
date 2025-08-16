package org.hzai.system.sysmenu.vo;

import lombok.Data;

import java.util.List;

@Data
public class MenuMeta {

	/**
	 * 菜单名称
	 */
	private String title;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 是否在菜单中显示
	 */
	private Boolean showParent;

	/**
	 * 菜单排序，值越高排的越后（只针对顶级路由）
	 */
	private Integer rank;

	private List<String> roles;

	private List<String> auths;

}
