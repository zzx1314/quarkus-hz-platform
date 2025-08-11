package org.hzai.util;

public interface CommonConstants {

	/**
	 * 删除
	 */
	String STATUS_DEL = "1";

	/**
	 * 正常
	 */
	String STATUS_NORMAL = "0";

	/**
	 * 未锁定
	 */
	Integer USER_STATUS_NORMAL = 1;

	/**
	 * 锁定
	 */
	Integer USER_STATUS_LOCK = 2;

	/**
	 * 菜单
	 */
	Integer MENU = 1;

	/**
	 * 按钮
	 */
	Integer ACTION_BUTTON = 2;

	/**
	 * 编码
	 */
	String UTF8 = "UTF-8";

	/**
	 * JSON 资源
	 */
	String CONTENT_TYPE = "application/json; charset=utf-8";

	/**
	 * 成功标记
	 */
	Integer SUCCESS = 10200;

	/**
	 * 失败标记
	 */
	Integer FAIL = 10400;

	/**
	 * 后端工程名
	 */
	String BACK_END_PROJECT = "th";

	/**
	 * 安全策略
	 */
	String SYS_SECURITY_POLICY = "sys_security_policy";

	/**
	 * 用户登录密码错误锁定时长（分钟）
	 */
	String SYS_LOGIN_LOCKTIME = "sysLoginMaxLockTime";

	/**
	 * 用户登录密码错误最大尝试次数
	 */
	String SYS_LOGIN_MAXTRYCOUNT = "sysLoginMaxTryCount";

	/**
	 * 登录超时
	 */
	String SYS_OVER_TIME = "sysOvertime";

	/**
	 * 密码修改周期
	 */
	String SYS_PASS_CHANE = "sysPassChange";

	/**
	 * headers中版本信息
	 */
	String VERSION = "VERSION";

	/**
	 * header 中租户ID
	 */
	String TENANT_ID = "TENANT-ID";

	/**
	 * 默认租户ID
	 */
	Integer TENANT_ID_1 = 1;

	/**
	 * 当前页
	 */
	String CURRENT = "current";

	/**
	 * size
	 */
	String SIZE = "size";

	/**
	 * 请求开始时间
	 */
	String REQUEST_START_TIME = "REQUEST-START-TIME";

}
