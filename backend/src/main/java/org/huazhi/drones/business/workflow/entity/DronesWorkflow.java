package org.huazhi.drones.business.workflow.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;

@Entity

public class DronesWorkflow extends PanacheEntityBase {
	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 工作流id
	 */
	private String uuid;

	/**
	 * 节点
	 */
	@Column(columnDefinition = "TEXT")
	private String nodes;

	/**
	 * 边
	 */
	@Column(columnDefinition = "TEXT")
	private String edges;

	/**
	 * 任务ID
	 */
	private Long taskId;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;

	/**
	 * 逻辑删除
	 */
	@Column(columnDefinition = "INT DEFAULT 0",  insertable = false)
	private Integer isDeleted;

	/**
	 * 命令字符串
	 */
	@Column(columnDefinition = "TEXT")
	private String commandJsonString;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCommandJsonString() {
		return commandJsonString;
	}

	public void setCommandJsonString(String commandJsonString) {
		this.commandJsonString = commandJsonString;
	}
}
