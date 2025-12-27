package org.huazhi.ai.aistatistics.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.huazhi.ai.aiapplication.service.AiApplicationService;
import org.huazhi.ai.aidocument.service.AiDocumentService;
import org.huazhi.ai.aiknowledgebase.service.AiKnowledgeBaseService;
import org.huazhi.ai.aimcp.service.AiMcpService;
import org.huazhi.ai.aistatistics.entity.BarChartData;
import org.huazhi.ai.aistatistics.entity.ChartData;
import org.huazhi.ai.aistatistics.entity.DocChatData;
import org.huazhi.util.R;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/aiStatistics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiStatisticsController {
    @Inject
    AiDocumentService aiDocumentService;

    @Inject
    AiKnowledgeBaseService aiKnowledgeBaseService;

    @Inject
    AiApplicationService aiApplicationService;

    @Inject
    AiMcpService aiMcpService;
    

    @GET
    @Path("/statisticsAll")
	public R<Object> statistics() {
        List<ChartData> chartDataList = new ArrayList<>();
		// 知识库统计
		ChartData knowledgeBaseCount = getKnowledgeBaseCount();
		chartDataList.add(knowledgeBaseCount);
		// MCP统计
		ChartData mcpCount = getMcpCount();
		chartDataList.add(mcpCount);
		// 应用统计
		ChartData applicationCount = getApplicationCount();
		chartDataList.add(applicationCount);
		// 文档统计
		ChartData documentCount = getDocumentCount();
		chartDataList.add(documentCount);
		return R.ok(chartDataList);
    }

    /**
	 * 统计一周内，知识库，MCP，应用，文档创建的数量
	 */
    @GET
	@Path("statisticsAllType")
	public R<Object> statisticsAllNum() {
        List<BarChartData> barChartDataList = new ArrayList<>();
		// 上周的数据
		BarChartData barChartDataBefore = new BarChartData();
		List<Long> knowledgeBaseCountBefore = aiKnowledgeBaseService.getKnowledgeBaseCountBefore();
		barChartDataBefore.setKnowledgeBaseCount(knowledgeBaseCountBefore);
		List<Long> mcpCountBefore = aiMcpService.getMcpCountBefore();
		barChartDataBefore.setMcpCount(mcpCountBefore);
		List<Long> documentCountBefore = aiDocumentService.getDocumentCountBefore();
		barChartDataBefore.setDocumentCount(documentCountBefore);
		List<Long> applicationCountBefore = aiApplicationService.getApplicationCountBefore();
		barChartDataBefore.setApplicationCount(applicationCountBefore);
		barChartDataList.add(barChartDataBefore);
		// 本周的数据
		BarChartData barChartData = new BarChartData();
		List<Long> knowledgeBaseCount = aiKnowledgeBaseService.getKnowledgeBaseCount();
		barChartData.setKnowledgeBaseCount(knowledgeBaseCount);
		List<Long> mcpCount = aiMcpService.getMcpCount();
		barChartData.setMcpCount(mcpCount);
		List<Long> documentCount = aiDocumentService.getDocumentCount();
		barChartData.setDocumentCount(documentCount);
		List<Long> applicationCount = aiApplicationService.getApplicationCount();
		barChartData.setApplicationCount(applicationCount);
		barChartDataList.add(barChartData);
		return R.ok(barChartDataList);
    }

    /**
	 * 统计在知识库中文档的数量
	 */
    @GET
	@Path("statisticsDocNumber")
	public R<Object> statisticDocumentCountInKb() {
        List<Map<String, Object>> result = aiDocumentService.countDocumentsByKnowledgeBase();

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
	 * 获取 MCP 统计数据
	 */
	private ChartData getMcpCount() {
		ChartData chartData = new ChartData();
		List<Long> mcpCountList = aiMcpService.getMcpCount();
		long totalMcpCount = aiMcpService.count();

		Long todayMcpCount = 0L;
		if (!mcpCountList.isEmpty()) {
			todayMcpCount = mcpCountList.get(6); // 最后一天是今天
		}
		double ratio = ((double) todayMcpCount / totalMcpCount) * 100;

		chartData.setName("MCP");
		chartData.setIcon("Question");
		chartData.setBgColor("#fff5f4");
		chartData.setColor("#e85f33");
		chartData.setDuration(1600);
		chartData.setValue(totalMcpCount);
		chartData.setData(mcpCountList);
		chartData.setPercent("+" + String.format("%.2f", ratio) + "%");
		return chartData;
	}

	/**
	 * 获取应用统计
	 */
	private ChartData getApplicationCount() {
		ChartData chartData = new ChartData();
		List<Long> applicationCountList = aiApplicationService.getApplicationCount();
		long totalApplicationCount = aiApplicationService.count(); // 获取总数量

		Long todayApplicationCount = 0L;
		if (!applicationCountList.isEmpty()) {
			todayApplicationCount = applicationCountList.get(6); // 最后一天是今天
		}

		double ratio = ((double) todayApplicationCount / totalApplicationCount) * 100;

		chartData.setName("应用");
		chartData.setIcon("AppstoreLine");
		chartData.setBgColor("#eff8f4");
		chartData.setColor("#26ce83");
		chartData.setDuration(1800);
		chartData.setValue(totalApplicationCount);
		chartData.setData(applicationCountList);
		chartData.setPercent("+" + String.format("%.2f", ratio) + "%");

		return chartData;
	}


    /**
	 * 获取知识库统计
	 */
	private ChartData getKnowledgeBaseCount() {
		ChartData chartData = new ChartData();
		List<Long> knowledgeBaseCount = aiKnowledgeBaseService.getKnowledgeBaseCount();
		long countNum = aiKnowledgeBaseService.count();
		Long today = knowledgeBaseCount.get(6);
		double ratio = ((double) today / countNum) * 100;
		chartData.setName("知识库");
		chartData.setIcon("GroupLine");
		chartData.setBgColor("#effaff");
		chartData.setColor("#41b6ff");
		chartData.setDuration(2200);
		chartData.setValue(countNum);
		chartData.setData(knowledgeBaseCount);
		chartData.setPercent("+" + String.format("%.2f", ratio) + "%");
		return chartData;
	}

    /**
	 * 获取文档统计
	 */
	private ChartData getDocumentCount() {
		ChartData chartData = new ChartData();
		List<Long> documentCountList = aiDocumentService.getDocumentCount();
		long totalDocumentCount = aiDocumentService.count(); // 获取总数量

		Long todayDocumentCount = 0L;
		if (!documentCountList.isEmpty()) {
			todayDocumentCount = documentCountList.get(6); // 最后一天是今天
		}

		double ratio = ((double) todayDocumentCount / totalDocumentCount) * 100;

		chartData.setName("文档");
		chartData.setIcon("FileTextLine"); // 图标名称可自定义
		chartData.setBgColor("#fffaf0");
		chartData.setColor("#ff9900");
		chartData.setDuration(1900);
		chartData.setValue(totalDocumentCount);
		chartData.setData(documentCountList);
		chartData.setPercent("+" + String.format("%.2f", ratio) + "%");

		return chartData;
	}
}
