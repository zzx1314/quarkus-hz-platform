package org.huazhi.system.sysorg.entity.vo;


import java.time.LocalDateTime;


public class SysOrgVo {

	/**
	 * 主键
	 */
	private Long id;

	private Long value;

	/**
	 * 组织名称
	 */
	private String name;

	private String label;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 父ID
	 */
	private Integer parentId;

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

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

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

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
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

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
