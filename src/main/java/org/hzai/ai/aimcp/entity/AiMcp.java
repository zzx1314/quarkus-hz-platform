package org.hzai.ai.aimcp.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * MCP服务
 * </p>
 *
 * @author zzx
 * @since 2025-06-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AiMcp extends PanacheEntityBase {

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	private String description;

	private String enable;

	private String commandType;

	private String command;

	private String status;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;

	private String mcpFilePath;

	private String originFileName;

	private Integer toolNum;


}
