package org.hzai.ai.aidocument.entity.dto;
import lombok.Data;

import java.util.List;

@Data
public class AiDocumentStoreDto {
	/**
	 * 文档id
	 */
	Long id;
	/**
	 * 文档id
	 */
	Long knowledgeBaseId;

	/**
	 * 分割策略
	 */
	String strategy;

	/**
	 * 分割标志
	 */
	String flag;

	/**
	 * 分割长度
	 */
	String length;

	/**
	 * 文件名
	 */
	String fileName;


	List<ParagraphsDto> paragraphs;


	String tempFilePath;
}
