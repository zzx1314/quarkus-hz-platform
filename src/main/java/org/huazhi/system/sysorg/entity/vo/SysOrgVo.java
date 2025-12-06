package org.huazhi.system.sysorg.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysOrgVo {

	/**
	 * 主键
	 */
	private Integer id;

	private Integer value;

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

}
