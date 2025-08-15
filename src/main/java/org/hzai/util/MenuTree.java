package org.hzai.util;

import org.hzai.system.sysmenu.entity.vo.MenuVo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends TreeNode {

	private String path;

	private String name;

	private MenuMeta meta;

	// private String component;

	// private String redirect;

	public MenuTree() {
	}

	public MenuTree(int id, String name, int parentId) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
	}

	public MenuTree(int id, String name, MenuTree parent) {
		this.id = id;
		this.parentId = parent.getId();
		this.name = name;
	}

	public MenuTree(MenuVo menuVo) {
		this.id = menuVo.getId();
		this.parentId = menuVo.getParentId();
		this.name = menuVo.getName();
		this.path = menuVo.getPathUrl();
		// this.component = menuVo.getComponent();
		// this.redirect = menuVo.getRedirect();
		this.meta = menuVo.getMeta();
	}

}
