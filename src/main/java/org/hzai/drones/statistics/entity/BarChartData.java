package org.hzai.drones.statistics.entity;

import lombok.Data;

import java.util.List;

@Data
public class BarChartData {
	public List<Long> deviceCount;

	public List<Long> taskCount;

	public List<Long> routeCount;

	public List<Long> modelCount;
}
