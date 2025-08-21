package org.hzai.ai.aimodel.entity;

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
 * 模型
 * </p>
 *
 * @author zzx
 * @since 2025-07-16
 */
@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AiModel extends PanacheEntityBase {

	@Id
	@GeneratedValue
	private Long id;

	private String modelName;

	private String baseUrl;

	private String apiKey;

	private String modelType;

	private String enable;

	private String remark;

	private Double temperature;

	private Integer maxTokens;

	private Double frequencyPenalty;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;


}
