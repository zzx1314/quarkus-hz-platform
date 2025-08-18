package org.hzai.system.sysdict.entity;

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
@Table(name = "p_sys_dict")
@EqualsAndHashCode(callSuper=false)
public class SysDict extends PanacheEntityBase{

	/**
	 * 主键
	 */
	@Id
    @GeneratedValue
	private Integer id;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 字典类型
	 */
	private String dictType;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	/**
	 * 1 表示删除，0 表示未删除
	 */
	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;

}
