package org.huazhi.system.sysorg.entity.dto;


public class SysOrgDto {
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
	private Integer parentId;

	/**
	 * 父节点名称
	 */
	private String parentName;

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

	
}
