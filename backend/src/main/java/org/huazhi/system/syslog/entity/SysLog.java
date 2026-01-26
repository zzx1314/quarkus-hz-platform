package org.huazhi.system.syslog.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "p_sys_logrecord")
public class SysLog extends PanacheEntityBase {

	/**
	 * id
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 租户
	 */
	private String tenant;

	/**
	 * 保存的操作日志的类型，比如：订单类型、商品类型
	 */
	private String type;

	/**
	 * 日志的子类型，比如订单的C端日志，和订单的B端日志，type都是订单类型，但是子类型不一样
	 */
	private String subType;

	/**
	 * 日志绑定的业务标识
	 */
	private String bizNo;

	/**
	 * 操作人
	 */
	private String operator;

	/**
	 * 日志内容
	 */
	private String action;

	/**
	 * 记录是否是操作失败的日志
	 */
	private boolean fail;

	/**
	 * 日志的创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 日志的额外信息
	 */
	private String extra;

	private String codeVariable;

	private String ip;

	@Column(columnDefinition = "INT DEFAULT 0", insertable = false)
	private Integer isDeleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getBizNo() {
		return bizNo;
	}

	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isFail() {
		return fail;
	}

	public void setFail(boolean fail) {
		this.fail = fail;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getCodeVariable() {
		return codeVariable;
	}

	public void setCodeVariable(String codeVariable) {
		this.codeVariable = codeVariable;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SysLog sysLog = (SysLog) o;
		return Objects.equals(id, sysLog.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "SysLog{" +
				"id=" + id +
				", tenant='" + tenant + '\'' +
				", type='" + type + '\'' +
				", subType='" + subType + '\'' +
				", bizNo='" + bizNo + '\'' +
				", operator='" + operator + '\'' +
				", action='" + action + '\'' +
				", fail=" + fail +
				", createTime=" + createTime +
				", extra='" + extra + '\'' +
				", codeVariable='" + codeVariable + '\'' +
				", ip='" + ip + '\'' +
				", isDeleted=" + isDeleted +
				'}';
	}

}
