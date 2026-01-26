package org.huazhi.drones.business.statistics.entity;


import java.util.List;


public class ChartData {
	private String icon;

	private String bgColor;

	private String color;

	private Integer duration;

	private String name;

	private Long value;

	private String percent;

	private List<Long> data;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public List<Long> getData() {
		return data;
	}

	public void setData(List<Long> data) {
		this.data = data;
	}

}
