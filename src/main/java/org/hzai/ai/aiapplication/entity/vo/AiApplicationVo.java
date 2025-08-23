package org.hzai.ai.aiapplication.entity.vo;


import java.util.List;

import org.hzai.ai.aiapplication.entity.AiApplication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AiApplicationVo extends AiApplication {

	private Integer mcpCount;

	private Integer knowledgeCount;

	private List<Integer> mcpIdList;

	private List<Integer> knowledgeIdList;

	private List<String> roleIdList;

	private String createUsername;
}
