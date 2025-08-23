package org.hzai.ai.aiprocess.entity;

import lombok.Data;

@Data
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
}
