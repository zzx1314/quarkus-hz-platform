package org.huazhi.ai.aiapplication.entity.vo;


import java.util.List;

import org.huazhi.ai.aiapplication.entity.AiApplication;

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

	private List<Long> roleIdList;

	private String createUsername;
}
