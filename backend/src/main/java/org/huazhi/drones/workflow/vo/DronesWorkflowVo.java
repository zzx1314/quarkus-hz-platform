package org.huazhi.drones.workflow.vo;

import java.util.List;
import java.util.Map;

import org.huazhi.drones.workflow.entity.NodeEntity;

import lombok.Data;

@Data
public class DronesWorkflowVo {

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

	// 建立 targetId -> List<sourceId> 的映射
	Map<String, List<String>> reverseEdgeMap;

}
