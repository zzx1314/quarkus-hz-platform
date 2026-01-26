package org.huazhi.system.sysuser.entity.dto;

import java.util.List;

import jakarta.ws.rs.QueryParam;

public class SysUserQueryDto {
	@QueryParam("username")
	private String username;

	@QueryParam("orgId")
	private Integer orgId;

	@QueryParam("orgIds")
	private List<Integer> orgIds;

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

	@QueryParam("beginTime")
	private String beginTime;

	@QueryParam("endTime")
	private String endTime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public List<Integer> getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List<Integer> orgIds) {
		this.orgIds = orgIds;
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

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	
}
