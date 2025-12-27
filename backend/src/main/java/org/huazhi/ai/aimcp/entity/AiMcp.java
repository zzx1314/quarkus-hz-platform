package org.huazhi.ai.aimcp.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AiMcp extends PanacheEntityBase {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String description;

	private String enable;

	private String commandType;

	private String command;

	private String status;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;

	private String mcpFilePath;

	private String originFileName;

	private Integer toolNum;


}
