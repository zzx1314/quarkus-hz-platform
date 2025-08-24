package org.hzai.ai.aimcptools.entity.dto;

import lombok.Data;

@Data
public class AiMcpToolsDto {
    private Long id;

    private String name;

	private String description;

	private String parameters;

	private String enable;

    private Long mcpId;

}