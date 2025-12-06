package org.huazhi.drones.workflow.entity.dto;

import lombok.Data;

@Data
public class DronesWorkflowDto {
	private Long id;

	/**
	 * 节点
	 */
	private String nodes;

	/**
	 * 边
	 */
	private String edges;

	/**
	 * 任务ID
	 */
	private Long taskId;
}