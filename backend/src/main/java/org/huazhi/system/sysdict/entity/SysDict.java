package org.huazhi.system.sysdict.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.huazhi.system.sysdictitem.entity.SysDictItem;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "p_sys_dict")
public class SysDict extends PanacheEntityBase {

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 字典类型
	 */
	private String dictType;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	private Boolean allowDeletion;

	private Boolean isShow;

	/**
	 * 1 表示删除，0 表示未删除
	 */
	@Column(columnDefinition = "INT DEFAULT 0",  insertable = false)
	private Integer isDeleted;

	@OneToMany(mappedBy = "sysDict", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<SysDictItem> sysDictItems;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Boolean getAllowDeletion() {
		return allowDeletion;
	}

	public void setAllowDeletion(Boolean allowDeletion) {
		this.allowDeletion = allowDeletion;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public List<SysDictItem> getSysDictItems() {
		return sysDictItems;
	}

	public void setSysDictItems(List<SysDictItem> sysDictItems) {
		this.sysDictItems = sysDictItems;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SysDict sysDict = (SysDict) o;
		return Objects.equals(id, sysDict.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "SysDict{" +
				"id=" + id +
				", type='" + type + '\'' +
				", dictType='" + dictType + '\'' +
				", description='" + description + '\'' +
				", remarks='" + remarks + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", allowDeletion=" + allowDeletion +
				", isShow=" + isShow +
				", isDeleted=" + isDeleted +
				'}';
	}

}
