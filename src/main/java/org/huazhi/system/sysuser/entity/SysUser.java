package org.huazhi.system.sysuser.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.huazhi.system.sysrole.entity.SysRole;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "p_sys_user")
@EqualsAndHashCode(callSuper = false)
public class SysUser extends PanacheEntityBase {
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 账号
	 */
	private String username;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 邮件
	 */
	private String email;

	/**
	 * 姓名
	 */
	private String realName;

	/**
	 * 密码
	 */
	@JsonIgnore
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
	private Integer isDeleted = 0;

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

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "p_sys_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@JsonIgnore
	public List<SysRole> roles = new ArrayList<>();

}
