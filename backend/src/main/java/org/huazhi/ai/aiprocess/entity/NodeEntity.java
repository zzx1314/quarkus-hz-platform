package org.huazhi.ai.aiprocess.entity;

import io.vertx.core.json.JsonObject;
import lombok.Data;

@Data
public class NodeEntity {
	private String id;

	private String label;

	private String type;

	private JsonObject data;
}
