package org.hzai.system.rolemenu.entity;

import lombok.Data;

import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Data
@Table(name = "p_sys_role_menu")
public class SysRoleMenu  {

	/**
	 * 主键
	 */
	@Id
    @GeneratedValue
	private Integer id;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 菜单id
	 */
	private Integer menuId;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	/**
	 * 1 表示删除，0 表示未删除
	 */
	// @TableLogic
	private Integer isDeleted;

	/**
	 * 备注
	 */
	private String remarks;

}
