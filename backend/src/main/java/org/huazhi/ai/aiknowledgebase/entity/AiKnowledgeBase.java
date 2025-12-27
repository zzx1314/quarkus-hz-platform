package org.huazhi.ai.aiknowledgebase.entity;

import java.time.LocalDateTime;

import org.huazhi.ai.aidocument.entity.AiDocument;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonFormat;

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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@Column(columnDefinition = "INT DEFAULT 0", insertable = false)
	private Integer isDeleted;

	private Integer acquaintanceLevel;

	private String hitHandle;


	@Transient
	private Long documentCount;

	public Long getDocumentCount() {
		if (documentCount == null) {
			documentCount = AiDocument.count("select count(d) from AiDocument d where d.knowledgeId = ?1", this.id);
		}
		return documentCount;
	}



}
