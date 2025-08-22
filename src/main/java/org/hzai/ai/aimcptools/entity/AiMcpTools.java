package org.hzai.ai.aimcptools.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AiMcpTools extends PanacheEntityBase {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String description;

	private String parameters;

	private String enable;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;

	private Long mcpId;


}
