package org.hzai.system.userrole.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name = "p_sys_user_role")
public class SysUserRole extends PanacheEntity{

	/**
	 * 主键
	 */
	@Id
    @GeneratedValue
	private Integer id;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	/**
	 * 1 表示删除，0 表示未删除
	 */
	private Integer isDeleted;

	/**
	 * 备注
	 */
	private String remarks;

}
