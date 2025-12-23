package org.huazhi.drones.business.workflow.entity;

import lombok.Data;

/**
 * 节点实体
 */
@Data
public class NodeEntity {
	private String id;

	private String label;

	private String type;

	NodeEntityData data;
}
