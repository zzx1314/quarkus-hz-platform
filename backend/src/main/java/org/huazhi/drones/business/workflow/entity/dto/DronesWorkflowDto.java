package org.huazhi.drones.business.workflow.entity.dto;

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

	/**
	 * 命令JSON字符串
	 */
	private String commandJsonString;
}