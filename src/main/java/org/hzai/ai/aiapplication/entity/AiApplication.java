package org.hzai.ai.aiapplication.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class AiApplication  extends PanacheEntityBase {

	@Id
	@GeneratedValue
	private Integer id;

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

	private String roles;

	private Long createId;

}
