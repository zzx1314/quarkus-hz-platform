package org.hzai.ai.aiapplication.entity.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class AiApplicationDto {
    private Long id;

    private String name;

	private String description;

	private String type;

	private String model;

	private String aiRole;

	private String prompt;

	private Integer mixHistory;

	private Integer isDeleted;

	private String mcpIds;

	private String knowledgeIds;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	private Boolean isSetup;

	private Long createId;

    private List<String> mcpIdList;

	private List<String> knowledgeIdList;

	private List<Long> roleIdList;

	private Integer processId;

	/**
	 * 节点信息
	 */
	private String nodes;

	/**
	 * 边信息
	 */
	private String edges;

}