package org.huazhi.ai.aistatistics.entity;
import lombok.Data;

import java.util.List;

@Data
public class BarChartData {
	public List<Long> knowledgeBaseCount;

	public List<Long> documentCount;

	public List<Long> applicationCount;

	public List<Long> mcpCount;
}
