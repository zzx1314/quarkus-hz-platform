package org.huazhi.system.sysmenu.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.huazhi.system.sysrole.entity.SysRole;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "p_sys_menu")
@EqualsAndHashCode(callSuper = false)
public class SysMenu extends PanacheEntityBase {
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 编码
	 */
	private String code;

	/**
	 * 菜单权限标识
	 */
	private String permission;

	/**
	 * 菜单路径
	 */
	private String pathUrl;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 父菜单ID
	 */
	private Integer parentId;

	/**
	 * VUE页面
	 */
	private String component;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 状态：0-开启，1- 关闭
	 */
	private Integer keepAlive;

	/**
	 * 类型：1：菜单 2：按钮
	 */
	private Integer type;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;

	/**
	 * 1 表示删除，0 表示未删除
	 */
	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 叶子节点
	 */
	private Boolean leaf;

	/**
	 * 菜单所属角色
	 */
	private String roleCode;

	/**
	 * 是否禁用
	 */
	private Boolean disabled;

	private Integer findAuthId;

	@ManyToMany(mappedBy = "menus", fetch = FetchType.LAZY)
	@JsonIgnore
	public List<SysRole> roles = new ArrayList<>();

}
