package org.hzai.ai.aiparagraph.entity.dto;

import lombok.Data;

@Data
public class AiParagraphDto {
    private Long id;

    private Long knowledgeId;

    private String vectorId;

    private String content;
}