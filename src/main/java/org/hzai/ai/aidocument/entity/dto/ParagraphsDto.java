package org.hzai.ai.aidocument.entity.dto;

import dev.langchain4j.data.document.Metadata;
import lombok.Data;

@Data
public class ParagraphsDto {
	private String content;

	private Integer length;

	private String title;

	Metadata metadata;
}
