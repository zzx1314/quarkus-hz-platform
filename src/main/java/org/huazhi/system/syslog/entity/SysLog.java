package org.huazhi.system.syslog.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "p_sys_logrecord")
@EqualsAndHashCode(callSuper = false)
public class SysLog extends PanacheEntityBase {

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;

}
