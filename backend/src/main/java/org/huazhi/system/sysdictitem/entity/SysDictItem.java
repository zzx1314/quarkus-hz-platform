package org.huazhi.system.sysdictitem.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import org.huazhi.system.sysdict.entity.SysDict;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "p_sys_dict_item")
@EqualsAndHashCode(callSuper = false)
public class SysDictItem extends PanacheEntityBase {

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 字典项类型
	 */
	private String type;

	/**
	 * 字典类型
	 */
	private String dictType;

	/**
	 * 标签名
	 */
	private String label;

	/**
	 * 数据值
	 */
	private String value;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 描述
	 */
	private String description;

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
	@Column(columnDefinition = "INT DEFAULT 0", insertable = false)
	private Integer isDeleted;

	/**
	 * 备注
	 */
	private String remarks;

	private Boolean allowDeletion;

	/**
	 * 所属字典ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dict_id")
	private SysDict sysDict;

}
