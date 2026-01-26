package org.huazhi.drones.business.workflow.entity.dto;


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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getEdges() {
		return edges;
	}

	public void setEdges(String edges) {
		this.edges = edges;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getCommandJsonString() {
		return commandJsonString;
	}

	public void setCommandJsonString(String commandJsonString) {
		this.commandJsonString = commandJsonString;
	}

	
}