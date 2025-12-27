package org.huazhi.ai.aiknowledgebase.entity.vo;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AiKnowledgeBaseVo {
	
	private Long id;

	private String knowledgeBaseName;

	private String knowledgeBaseType;

	private String knowledgeBaseDesc;

	private String createUser;

	private LocalDateTime createTime;

	private Integer acquaintanceLevel;

	private String hitHandle;

	/**
	 * 文档数量
	 */
	private Long documentCount;

	/**
	 * 应用数量
	 */
	private Long appCount;

}
