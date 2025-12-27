package org.huazhi.ai.aimodel.entity.dto;

import lombok.Data;

@Data
public class AiModelDto {
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
}