package org.huazhi.system.sysorg.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "p_sys_org")
@EqualsAndHashCode(callSuper = false)
public class SysOrg extends PanacheEntityBase {

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 组织名称
	 */
	private String name;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 父ID
	 */
	private Long parentId;

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

	private String type;

	/**
	 * 是否展开节点
	 */
	private Boolean isOut;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;

	/**
	 * 1 表示删除，0 表示未删除
	 */
	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;

	/**
	 * 备注
	 */
	private String remarks;

}
