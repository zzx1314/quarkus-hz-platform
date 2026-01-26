package org.huazhi.drones.business.statistics.entity;


import java.util.List;


public class BarChartData {
	public List<Long> deviceCount;

	public List<Long> taskCount;

	public List<Long> routeCount;

	public List<Long> modelCount;

	public List<Long> getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(List<Long> deviceCount) {
		this.deviceCount = deviceCount;
	}

	public List<Long> getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(List<Long> taskCount) {
		this.taskCount = taskCount;
	}

	public List<Long> getRouteCount() {
		return routeCount;
	}

	public void setRouteCount(List<Long> routeCount) {
		this.routeCount = routeCount;
	}

	public List<Long> getModelCount() {
		return modelCount;
	}

	public void setModelCount(List<Long> modelCount) {
		this.modelCount = modelCount;
	}
}
