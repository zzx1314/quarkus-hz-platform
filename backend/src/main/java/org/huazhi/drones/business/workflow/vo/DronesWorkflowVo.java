package org.huazhi.drones.business.workflow.vo;

import java.util.List;
import java.util.Map;

import org.huazhi.drones.business.workflow.entity.NodeEntity;


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

    public NodeEntity getStartNode() {
        return startNode;
    }

    public void setStartNode(NodeEntity startNode) {
        this.startNode = startNode;
    }

    public Map<String, List<String>> getEdgeMap() {
        return edgeMap;
    }

    public void setEdgeMap(Map<String, List<String>> edgeMap) {
        this.edgeMap = edgeMap;
    }

    public Map<String, NodeEntity> getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(Map<String, NodeEntity> nodeMap) {
        this.nodeMap = nodeMap;
    }

    public Map<String, List<String>> getReverseEdgeMap() {
        return reverseEdgeMap;
    }

    public void setReverseEdgeMap(Map<String, List<String>> reverseEdgeMap) {
        this.reverseEdgeMap = reverseEdgeMap;
    }


	

}
