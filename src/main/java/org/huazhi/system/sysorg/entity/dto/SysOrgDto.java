package org.huazhi.system.sysorg.entity.dto;

import lombok.Data;

@Data
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
}
