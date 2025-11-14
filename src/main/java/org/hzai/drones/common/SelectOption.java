package org.hzai.drones.common;

import lombok.Data;

@Data
public class SelectOption {

	private String label;

	private Object value;

	public SelectOption(String label, Object value) {
		this.label = label;
		this.value = value;
	}

	public SelectOption() {
	}

	

}
