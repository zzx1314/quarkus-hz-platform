package org.huazhi.system.sysauth.vo;


import java.util.List;
import java.util.Set;


public class SysAuthMenuVo {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 是否全选
	 */
	private Boolean isCheckAll;

	/**
	 * 权限列表
	 */
	private List<SysAuthTitleVo> authList;

	/**
	 * 已使用的权限
	 */
	private Set<Long> useAuthList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getIsCheckAll() {
		return isCheckAll;
	}

	public void setIsCheckAll(Boolean isCheckAll) {
		this.isCheckAll = isCheckAll;
	}

	public List<SysAuthTitleVo> getAuthList() {
		return authList;
	}

	public void setAuthList(List<SysAuthTitleVo> authList) {
		this.authList = authList;
	}

	public Set<Long> getUseAuthList() {
		return useAuthList;
	}

	public void setUseAuthList(Set<Long> useAuthList) {
		this.useAuthList = useAuthList;
	}

	

}
