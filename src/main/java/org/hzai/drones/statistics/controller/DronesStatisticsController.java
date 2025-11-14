package org.hzai.drones.statistics.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hzai.drones.device.service.DronesDeviceService;
import org.hzai.drones.model.service.DronesModelService;
import org.hzai.drones.routelibrary.service.DronesRouteLibraryService;
import org.hzai.drones.statistics.entity.BarChartData;
import org.hzai.drones.statistics.entity.ChartData;
import org.hzai.drones.statistics.entity.DocChatData;
import org.hzai.drones.task.service.DronesTaskService;
import org.hzai.util.R;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/dronesStatistics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesStatisticsController {
    @Inject
    DronesDeviceService dronesDeviceService;

    @Inject
    DronesTaskService dronesTaskService;

    @Inject
    DronesRouteLibraryService dronesRouteLibraryService;

    @Inject
    DronesModelService dronesModelService;

    @GET
    @Path("/statisticsAll")
	public R<Object> statistics() {
        List<ChartData> chartDataList = new ArrayList<>();
		// 设备统计
		ChartData deviceData = getDeviceCount();
		chartDataList.add(deviceData);
		// 任务统计
		ChartData taskCount = getTaskCount();
		chartDataList.add(taskCount);
		// 航线统计
		ChartData routeCount = getRouteCount();
		chartDataList.add(routeCount);
		// 模型统计
		ChartData modelCount = getModelCount();
		chartDataList.add(modelCount);
		return R.ok(chartDataList);
    }

    /**
	 * 统计一周内创建的数量
	 */
    @GET
	@Path("statisticsAllType")
	public R<Object> statisticsAllNum() {
        List<BarChartData> barChartDataList = new ArrayList<>();
		// 上周的数据
		BarChartData barChartDataBefore = new BarChartData();
		List<Long> deviceCountBefore = dronesDeviceService.getDeviceCountBefore();
		barChartDataBefore.setDeviceCount(deviceCountBefore);
		List<Long> taskBefore = dronesTaskService.getTaskCountBefore();
		barChartDataBefore.setTaskCount(taskBefore);
		List<Long> routeCountBefore = dronesRouteLibraryService.getRouteCountBefore();
		barChartDataBefore.setRouteCount(routeCountBefore);
		List<Long> modelCountBefore = dronesModelService.getModelCountBefore();
		barChartDataBefore.setModelCount(modelCountBefore);
		barChartDataList.add(barChartDataBefore);
		// 本周的数据
		BarChartData barChartData = new BarChartData();
		List<Long> deviceCount = dronesDeviceService.getDeviceCount();
		barChartData.setDeviceCount(deviceCount);
		List<Long> taskCount = dronesTaskService.getTaskCount();
		barChartData.setTaskCount(taskCount);
		List<Long> routeCount = dronesRouteLibraryService.getRouteCount();
		barChartData.setRouteCount(routeCount);
		List<Long> modelCount = dronesModelService.getModelCount();
		barChartData.setModelCount(modelCount);
		barChartDataList.add(barChartData);
		return R.ok(barChartDataList);
    }

    /**
	 * 统计设备类型数量
	 */
    @GET
	@Path("statisticsDeviceTypeNumber")
	public R<Object> statisticsDeviceNumber() {
        List<Map<String, Object>> result = dronesDeviceService.statisticsDeviceTypeNumber();

		List<String> names = new ArrayList<>();
		List<Integer> counts = new ArrayList<>();

		for (Map<String, Object> map : result) {
			names.add((String) map.get("name"));
			counts.add(((Number) map.get("count")).intValue());
		}

		DocChatData docChatData = new DocChatData();
		docChatData.setXData(names);
		docChatData.setYData(counts);
		return R.ok(docChatData);
    }


     /**
	 * 获取 设备 统计数据
	 */
	private ChartData getDeviceCount() {
		ChartData chartData = new ChartData();
		List<Long> deviceCountList = dronesDeviceService.getDeviceCount();
		long totalDeviceCount = dronesDeviceService.count();

		Long todayDeviceCount = 0L;
		if (!deviceCountList.isEmpty()) {
			todayDeviceCount = deviceCountList.get(6); // 最后一天是今天
		}
		double ratio = ((double) todayDeviceCount / totalDeviceCount) * 100;

		chartData.setName("设备");
		chartData.setIcon("Question");
		chartData.setBgColor("#fff5f4");
		chartData.setColor("#e85f33");
		chartData.setDuration(1600);
		chartData.setValue(totalDeviceCount);
		chartData.setData(deviceCountList);
		chartData.setPercent("+" + String.format("%.2f", ratio) + "%");
		return chartData;
	}

    /**
	 * 获取任务统计
	 */
	private ChartData getTaskCount() {
		ChartData chartData = new ChartData();
		List<Long> taskCountList = dronesTaskService.getTaskCount();
		long totalCount = dronesTaskService.count(); // 获取总数量

		Long todayCount = 0L;
		if (!taskCountList.isEmpty()) {
			todayCount = taskCountList.get(6); // 最后一天是今天
		}

		double ratio = ((double) todayCount / totalCount) * 100;

		chartData.setName("任务");
		chartData.setIcon("AppstoreLine");
		chartData.setBgColor("#eff8f4");
		chartData.setColor("#26ce83");
		chartData.setDuration(1800);
		chartData.setValue(totalCount);
		chartData.setData(taskCountList);
		chartData.setPercent("+" + String.format("%.2f", ratio) + "%");

		return chartData;
	}


    /**
	 * 获取航线统计
	 */
	private ChartData getRouteCount() {
		ChartData chartData = new ChartData();
		List<Long> count = dronesRouteLibraryService.getRouteCount();
		long countNum = dronesRouteLibraryService.count();
		Long today = count.get(6);
		double ratio = ((double) today / countNum) * 100;
		chartData.setName("航线");
		chartData.setIcon("GroupLine");
		chartData.setBgColor("#effaff");
		chartData.setColor("#41b6ff");
		chartData.setDuration(2200);
		chartData.setValue(countNum);
		chartData.setData(count);
		chartData.setPercent("+" + String.format("%.2f", ratio) + "%");
		return chartData;
	}

    /**
	 * 获取模型统计
	 */
	private ChartData getModelCount() {
		ChartData chartData = new ChartData();
		List<Long> countList = dronesModelService.getModelCount();
		long total = dronesModelService.count(); // 获取总数量

		Long today = 0L;
		if (!countList.isEmpty()) {
			today = countList.get(6); // 最后一天是今天
		}

		double ratio = ((double) today / total) * 100;

		chartData.setName("模型");
		chartData.setIcon("FileTextLine"); // 图标名称可自定义
		chartData.setBgColor("#fffaf0");
		chartData.setColor("#ff9900");
		chartData.setDuration(1900);
		chartData.setValue(total);
		chartData.setData(countList);
		chartData.setPercent("+" + String.format("%.2f", ratio) + "%");

		return chartData;
	}

}
