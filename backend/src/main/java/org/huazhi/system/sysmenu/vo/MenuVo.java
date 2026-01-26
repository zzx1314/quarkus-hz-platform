package org.huazhi.system.sysmenu.vo;


import java.io.Serializable;
import java.util.List;

import org.huazhi.util.MenuMeta;

/**
 * @Classname MenuVO
 * @Description 菜单权限
 * @Author zhangzexin
 * @Date 2022-02-19 11:54
 * @Version 1.0
 */
public class MenuVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	private Integer id;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单编码
	 */
	private String code;

	/**
	 * 菜单权限标识
	 */
	private String permission;

	/**
	 * 父菜单ID
	 */
	private Integer parentId;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 一个路径
	 */
	private String pathUrl;

	/**
	 * VUE页面
	 */
	private String component;

	/**
	 * 排序值
	 */
	private Integer sort;

	/**
	 * 菜单类型 （1菜单 2按钮）
	 */
	private Integer type;

	/**
	 * 叶子节点
	 */
	private Boolean leaf;

	/**
	 * 路由跳转
	 */
	private String redirect;

	/**
	 * 菜单信息
	 */
	private MenuMeta meta;

	/**
	 * 菜单权限标识
	 */
	private List<String> auths;

	/**
	 * 菜单所属角色
	 */
	private List<String> roles;

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/**
	 * menuId 相同则相同
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MenuVo) {
			Integer targetMenuId = ((MenuVo) obj).getId();
			return id.equals(targetMenuId);
		}
		return super.equals(obj);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPathUrl() {
		return pathUrl;
	}

	public void setPathUrl(String pathUrl) {
		this.pathUrl = pathUrl;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public MenuMeta getMeta() {
		return meta;
	}

	public void setMeta(MenuMeta meta) {
		this.meta = meta;
	}

	public List<String> getAuths() {
		return auths;
	}

	public void setAuths(List<String> auths) {
		this.auths = auths;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
