package org.huazhi.system.sysorg.entity.dto;

import java.time.LocalDateTime;

import org.huazhi.util.TreeNode;



public class SysOrgTreeDto extends TreeNode {

	private String name;

	private Integer sort;

	private LocalDateTime createTime;

	private String parentName;

	private String orgDuty;

	private String desrc;

	private String type;

	private Boolean isOut;

	private Integer isDeleted;

	private String remarks;

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

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
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
