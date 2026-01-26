package org.huazhi.drones.business.workflow.entity;


/**
 * 边实体
 */
public class EdgeEntity {
	private String id;

	/**
	 * 源头id
	 */
	private String source;

	/**
	 * 目标id
	 */
	private String target;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 源头id
	 */
	public String getSource() {
		return source;
	}

	/**
	 * 源头id
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * 目标id
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * 目标id
	 */
	public void setTarget(String target) {
		this.target = target;
	}
}
