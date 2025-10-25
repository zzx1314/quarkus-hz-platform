package org.hzai.drones.workflow.entity;

import io.vertx.core.json.JsonObject;
import lombok.Data;

/**
 * 节点实体
 */
@Data
public class NodeEntity {
	private String id;

	private String label;

	private String type;

	private JsonObject data;
}
