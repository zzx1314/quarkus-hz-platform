package org.huazhi.util;


import java.util.List;


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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Boolean getShowParent() {
		return showParent;
	}

	public void setShowParent(Boolean showParent) {
		this.showParent = showParent;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getAuths() {
		return auths;
	}

	public void setAuths(List<String> auths) {
		this.auths = auths;
	}

	

}
