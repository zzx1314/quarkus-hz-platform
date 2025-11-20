package org.huazhi.drones.workflow.entity;

import lombok.Data;

/**
 * 节点实体
 */
@Data
public class NodeEntity {
	private String id;

	private String deviceId;

	private Long routeId;

	private String label;

	private String type;

	

	/**
	 * 起飞
	 */
	private DeviceNodeTakeoff takeoff;

	/**
	 * 行为
	 */
	private DeviceNodeAction action;

	/**
	 * 降落
	 */
	private DeviceNodeLand land;
}
