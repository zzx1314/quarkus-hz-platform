package org.huazhi.ai.aiknowledgebase.entity.dto;

import lombok.Data;

@Data
public class AiKnowledgeBaseDto {
    private Long id;

    private String knowledgeBaseName;

	private String knowledgeBaseType;

	private String knowledgeBaseDesc;
}