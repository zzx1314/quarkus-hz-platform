package org.huazhi.drones.workflow.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class DronesWorkflow extends PanacheEntityBase{
	/**
	 * 主键ID
	 */
    @Id
	@GeneratedValue
	private Long id;

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
	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;
}
