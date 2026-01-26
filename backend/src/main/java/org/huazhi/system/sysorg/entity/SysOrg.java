package org.huazhi.system.sysorg.entity;


import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "p_sys_org")
public class SysOrg extends PanacheEntityBase {

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 组织名称
	 */
	private String name;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 父ID
	 */
	private Long parentId;

	/**
	 * 父节点名称
	 */
	private String parentName;

	/**
	 * 部门职责
	 */
	private String orgDuty;

	/**
	 * 部门描述
	 */
	private String desrc;

	private String type;

	/**
	 * 是否展开节点
	 */
	private Boolean isOut;

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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getOrgDuty() {
		return orgDuty;
	}

	public void setOrgDuty(String orgDuty) {
		this.orgDuty = orgDuty;
	}

	public String getDesrc() {
		return desrc;
	}

	public void setDesrc(String desrc) {
		this.desrc = desrc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsOut() {
		return isOut;
	}

	public void setIsOut(Boolean isOut) {
		this.isOut = isOut;
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

}
