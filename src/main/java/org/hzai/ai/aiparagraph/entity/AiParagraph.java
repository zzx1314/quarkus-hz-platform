package org.hzai.ai.aiparagraph.entity;

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
 * 段落
 * </p>
 *
 * @author zzx
 * @since 2025-06-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class AiParagraph extends PanacheEntityBase {

	@Id
	@GeneratedValue
	private Integer id;

	private String content;

	private Integer characterNumber;

	private Long docId;

	private Boolean isSetup;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;

	private String vectorId;

	private String metadata;


}
