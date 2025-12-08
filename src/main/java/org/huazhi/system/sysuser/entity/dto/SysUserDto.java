package org.huazhi.system.sysuser.entity.dto;

import java.util.List;

import org.huazhi.system.sysuser.entity.SysUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserDto extends SysUser {
    private Long id;

    private String username;

    private List<Long> roleIdList;

    private List<String> permissions;

    /**
	 * 角色ID
	 */
	private Long role;

	/**
	 * 组织机构ids
	 */
	private List<Long> orgIds;

    /**
	 * 新密码
	 */
	private String newpassword;

	/**
	 * 确认密码
	 */
	private String newpassword1;
}
