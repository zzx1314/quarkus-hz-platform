package org.huazhi.util;

import org.huazhi.system.sysmenu.entity.vo.MenuVo;

public class MenuTree extends TreeNode {

	private String path;

	private String name;

	private MenuMeta meta;

	// private String component;

	// private String redirect;

	public MenuTree() {
	}

	public MenuTree(Long id, String name, Long parentId) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
	}

	public MenuTree(Long id, String name, MenuTree parent) {
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MenuMeta getMeta() {
		return meta;
	}

	public void setMeta(MenuMeta meta) {
		this.meta = meta;
	}

	

}
