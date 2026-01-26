package org.huazhi.system.sysuser.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

@Entity
@Table(name = "p_sys_user")
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
	private String password;

	/**
	 * 组织ID
	 */
	private Long orgId;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public LocalDateTime getLockTime() {
		return lockTime;
	}

	public void setLockTime(LocalDateTime lockTime) {
		this.lockTime = lockTime;
	}

	public LocalDateTime getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getTryCount() {
		return tryCount;
	}

	public void setTryCount(Integer tryCount) {
		this.tryCount = tryCount;
	}

	public Integer getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(Integer lockFlag) {
		this.lockFlag = lockFlag;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDateTime getPassUpdateTime() {
		return passUpdateTime;
	}

	public void setPassUpdateTime(LocalDateTime passUpdateTime) {
		this.passUpdateTime = passUpdateTime;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public Integer getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Integer firstLogin) {
		this.firstLogin = firstLogin;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SysUser sysUser = (SysUser) o;
		return Objects.equals(id, sysUser.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "SysUser{" +
				"id=" + id +
				", username='" + username + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				", realName='" + realName + '\'' +
				", orgId=" + orgId +
				", lockTime=" + lockTime +
				", lastLoginTime=" + lastLoginTime +
				", tryCount=" + tryCount +
				", lockFlag=" + lockFlag +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", isDeleted=" + isDeleted +
				", remarks='" + remarks + '\'' +
				", passUpdateTime=" + passUpdateTime +
				", card='" + card + '\'' +
				", isShow=" + isShow +
				", enable=" + enable +
				", firstLogin=" + firstLogin +
				", sex='" + sex + '\'' +
				'}';
	}

}
