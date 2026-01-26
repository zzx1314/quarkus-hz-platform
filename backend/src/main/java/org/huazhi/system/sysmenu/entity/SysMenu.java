package org.huazhi.system.sysmenu.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

@Entity
@Table(name = "p_sys_menu")
public class SysMenu extends PanacheEntityBase {
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	private Long id;

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
	private Long parentId;

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
	@Column(columnDefinition = "INT DEFAULT 0",  insertable = false)
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getPathUrl() {
		return pathUrl;
	}

	public void setPathUrl(String pathUrl) {
		this.pathUrl = pathUrl;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

	public Integer getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(Integer keepAlive) {
		this.keepAlive = keepAlive;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Integer getFindAuthId() {
		return findAuthId;
	}

	public void setFindAuthId(Integer findAuthId) {
		this.findAuthId = findAuthId;
	}

	public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SysMenu sysMenu = (SysMenu) o;
		return Objects.equals(id, sysMenu.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "SysMenu{" +
				"id=" + id +
				", name='" + name + '\'' +
				", code='" + code + '\'' +
				", permission='" + permission + '\'' +
				", pathUrl='" + pathUrl + '\'' +
				", icon='" + icon + '\'' +
				", parentId=" + parentId +
				", component='" + component + '\'' +
				", sort=" + sort +
				", keepAlive=" + keepAlive +
				", type=" + type +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", isDeleted=" + isDeleted +
				", remarks='" + remarks + '\'' +
				", leaf=" + leaf +
				", roleCode='" + roleCode + '\'' +
				", disabled=" + disabled +
				", findAuthId=" + findAuthId +
				'}';
	}

}
