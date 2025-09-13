package org.hzai.ai.aidocument.entity;


import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文档
 * </p>
 *
 * @author zzx
 * @since 2025-06-16
 */
@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class AiDocument extends PanacheEntityBase {

	@Id
	@GeneratedValue
	private Long id;

	private Long knowledgeId;

	private String docName;

	private Integer characterNumber;

	private Integer sectionNumber;

	private String status;

	private String enableStatus;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;

	private String splitterStrategy;

	private String splitterLength;

	private String splitterFlag;



}
