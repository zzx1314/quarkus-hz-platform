package org.hzai.system.sysuser.entity.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Data;

@Data
public class SysUserQueryDto {
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

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}
