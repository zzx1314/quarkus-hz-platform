package org.hzai.system.rolemenu.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Data
@Table(name = "p_sys_role_menu")
@EqualsAndHashCode(callSuper=false)
public class SysRoleMenu extends PanacheEntityBase {

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
