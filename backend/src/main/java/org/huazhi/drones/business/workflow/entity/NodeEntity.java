package org.huazhi.drones.business.workflow.entity;


/**
 * 节点实体
 */
public class NodeEntity {
	private String id;

	private String label;

	private String type;

	NodeEntityData data;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public NodeEntityData getData() {
		return data;
	}

	public void setData(NodeEntityData data) {
		this.data = data;
	}
}
