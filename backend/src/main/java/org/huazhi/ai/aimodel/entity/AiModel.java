package org.huazhi.ai.aimodel.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@Column(columnDefinition = "INT DEFAULT 0", insertable = false)
	private Integer isDeleted;


}
