package org.hzai.ai.aiapplication.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hzai.system.sysuser.entity.SysUser;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用
 * </p>
 *
 * @author zzx
 * @since 2025-06-16
 */
@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class AiApplication  extends PanacheEntityBase {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String description;

	private String type;

	private String model;

	private String aiRole;

	private String prompt;

	private Integer mixHistory;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;

	private String mcpIds;

	private String knowledgeIds;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	private Boolean isSetup;

	@Column(columnDefinition = "bigint[]")
    private List<Long> roles;

	private Long createId;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createId", insertable = false, updatable = false)
    private SysUser sysUser;

}
