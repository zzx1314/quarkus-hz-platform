package org.hzai.user.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.ws.rs.QueryParam;

@Data
@Entity
@Table(name = "p_sys_user")
@EqualsAndHashCode(callSuper = false)
public class SysUser{
	/**
	 * 主键
	 */
	@Id
    @GeneratedValue
	private Long id;

	/**
	 * 账号
	 */
	@QueryParam("username")
	private String username;

	/**
	 * 电话
	 */
	@QueryParam("phone")
	private String phone;

	/**
	 * 邮件
	 */
	@QueryParam("email")
	private String email;

	/**
	 * 姓名
	 */
	@QueryParam("realName")
	private String realName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 组织ID
	 */
	private Integer orgId;

	/**
	 * 锁定时间
	 */
	private LocalDateTime lockTime;

	/**
	 * 上次登录时间
	 */
	private LocalDateTime lastLoginTime;

	/**
	 * 尝试次数
	 */
	private Integer tryCount;

	/**
	 * 锁定状态(1-正常，2-锁定)
	 */
	private Integer lockFlag;

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
	private Integer isDeleted;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 密码修改时间
	 */
	private LocalDateTime passUpdateTime;

	/**
	 * 身份证号
	 */
	private String card;

	/**
	 * 是否显示用户信息
	 */
	private Integer isShow;

	/**
	 * 启用状态(1-启用，2-停用)
	 */
	private Integer enable;

	/**
	 * 首次登录
	 */
	private Integer firstLogin;

	/**
	 * 性别
	 */
	private String sex;

}
