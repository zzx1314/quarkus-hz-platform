package org.huazhi.system.sysmenu.vo;

import lombok.Data;

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
@Data
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

}
