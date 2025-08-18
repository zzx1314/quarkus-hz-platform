package org.hzai.ai.aimcptools.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * mcp工具
 * </p>
 *
 * @author zzx
 * @since 2025-06-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AiMcpTools extends PanacheEntityBase {

	private Integer id;

	private String name;

	private String description;

	private String parameters;

	private String enable;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;

	private Integer mcpId;


}
