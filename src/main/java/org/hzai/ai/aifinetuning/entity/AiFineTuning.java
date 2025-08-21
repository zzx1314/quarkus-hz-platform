package org.hzai.ai.aifinetuning.entity;


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
 * 微调
 * </p>
 *
 * @author zzx
 * @since 2025-08-06
 */
@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class AiFineTuning extends PanacheEntityBase {

	@Id
	@GeneratedValue
	private Long id;

	private String taskName;

	private String modelNameOrPath;

	private String template;

	private String dataset;

	private String finetuningType;

	private String outputDir;

	private String maxSamples;

	private String valSize;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;
}
