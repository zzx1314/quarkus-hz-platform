package org.huazhi.system.sysdictitem.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.huazhi.system.sysdict.entity.SysDict;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "p_sys_dict_item")
public class SysDictItem extends PanacheEntityBase {

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 字典项类型
	 */
	private String type;

	/**
	 * 字典类型
	 */
	private String dictType;

	/**
	 * 标签名
	 */
	private String label;

	/**
	 * 数据值
	 */
	private String value;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 描述
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
	@Column(columnDefinition = "INT DEFAULT 0",  insertable = false)
	private Integer isDeleted;

	/**
	 * 备注
	 */
	private String remarks;

	private Boolean allowDeletion;

	/**
	 * 所属字典ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dict_id")
	private SysDict sysDict;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Boolean getAllowDeletion() {
		return allowDeletion;
	}

	public void setAllowDeletion(Boolean allowDeletion) {
		this.allowDeletion = allowDeletion;
	}

	public SysDict getSysDict() {
		return sysDict;
	}

	public void setSysDict(SysDict sysDict) {
		this.sysDict = sysDict;
	}


	

}
