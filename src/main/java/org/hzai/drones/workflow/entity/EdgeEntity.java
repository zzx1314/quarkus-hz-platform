package org.hzai.drones.workflow.entity;

import lombok.Data;

/**
 * 边实体
 */
@Data
public class EdgeEntity {
	private String id;

	/**
	 * 源头id
	 */
	private String source;

	/**
	 * 目标id
	 */
	private String target;
}
