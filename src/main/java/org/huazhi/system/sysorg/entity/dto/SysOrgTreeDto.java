package org.huazhi.system.sysorg.entity.dto;

import java.time.LocalDateTime;

import org.huazhi.util.TreeNode;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
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

}
