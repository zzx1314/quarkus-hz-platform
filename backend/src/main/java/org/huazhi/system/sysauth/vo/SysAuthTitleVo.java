package org.huazhi.system.sysauth.vo;

import lombok.Data;

@Data
public class SysAuthTitleVo {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 是否禁用
	 */
	private Boolean disabled;

}
