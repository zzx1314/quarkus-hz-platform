package org.hzai.system.org.entity.dto;

import org.hzai.util.TreeNode;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class OrgDto extends TreeNode {

	private String name;

	private Integer sort;

	private String parentName;

	private String orgDuty;

	private String desrc;

	private String type;

	private Boolean isOut;

	private Integer isDeleted;

	private String remarks;

}
