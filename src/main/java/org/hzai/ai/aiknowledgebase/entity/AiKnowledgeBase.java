package org.hzai.ai.aiknowledgebase.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;

/**
 * <p>
 * 知识库
 * </p>
 *
 * @author zzx
 * @since 2025-06-16
 */
@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AiKnowledgeBase extends PanacheEntityBase {

	@Id
	@GeneratedValue
	private Long id;

	private String knowledgeBaseName;

	private String knowledgeBaseType;

	private String knowledgeBaseDesc;

	private String createUser;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer isDeleted;

	private Integer acquaintanceLevel;

	private String hitHandle;


	@Formula("(select count(d.id) from ai_document d where d.knowledge_id = id)")
	private Long documentCount;


}
