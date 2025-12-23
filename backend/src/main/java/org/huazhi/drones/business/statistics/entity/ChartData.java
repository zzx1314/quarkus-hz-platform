package org.huazhi.drones.business.statistics.entity;

import lombok.Data;

import java.util.List;

@Data
public class ChartData {
	private String icon;

	private String bgColor;

	private String color;

	private Integer duration;

	private String name;

	private Long value;

	private String percent;

	private List<Long> data;

}
