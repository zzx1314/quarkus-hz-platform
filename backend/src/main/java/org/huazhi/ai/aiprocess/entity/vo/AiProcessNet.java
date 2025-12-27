package org.huazhi.ai.aiprocess.entity.vo;
import lombok.Data;

import java.util.List;
import java.util.Map;

import org.huazhi.ai.aiprocess.entity.NodeEntity;

@Data
public class AiProcessNet {
	/**
	 * 开始节点
	 */
	private NodeEntity startNode;

	/**
	 * 建立从 sourceId -> List<targetId> 的映射
	 */
	Map<String, List<String>> edgeMap;

	/**
	 * 构建 id -> NodeEntity 映射
	 */
	Map<String, NodeEntity> nodeMap;
}
