package org.huazhi.system.sysrole.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.huazhi.system.sysmenu.entity.SysMenu;
import org.huazhi.system.sysuser.entity.SysUser;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "p_sys_role")
@EqualsAndHashCode(callSuper = false)
public class SysRole extends PanacheEntityBase {

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 角色名
	 */
	private String name;

	/**
	 * 角色编码
	 */
	private String code;

	/**
	 * 角色描述
	 */
	private String description;

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
	@Column(columnDefinition = "INT DEFAULT 0", insertable = false)
	private Integer isDeleted;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 编辑
	 */
	private Boolean isEdit;

	/**
	 * 数据权限类型
	 */
	private Integer dsType;

	/**
	 * 数据权限作用范围
	 */
	private String dsScope;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "p_sys_role_menu", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "menu_id"))
	@JsonIgnore
	public List<SysMenu> menus = new ArrayList<>();

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	@JsonIgnore
	public List<SysUser> users = new ArrayList<>();

}
