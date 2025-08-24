package org.hzai.system.sysuser.entity.dto;

import java.util.List;

import jakarta.ws.rs.QueryParam;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
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
}
