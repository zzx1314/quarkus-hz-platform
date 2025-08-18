package org.hzai.ai.aistatistics.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * 获取近7天的日期
	 */
	public static List<String> getLastSevenDays() {
		List<String> dates = new ArrayList<>();
		LocalDate today = LocalDate.now();
		// 获取本周的星期一
		LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);

		// 从星期一到星期日（共7天）
		for (int i = 0; i < 7; i++) {
			LocalDate date = monday.plusDays(i);
			dates.add(date.format(DATE_FORMATTER));
		}
		return dates;
	}

	/**
	 * 获取从今天开始向前推14天到向前推7天的日期列表（包含起始和结束当天）
	 */
	public static List<String> getLast14DaysToLast7Days() {
		List<String> dates = new ArrayList<>();
		LocalDate today = LocalDate.now();

		// 获取本周的星期一
		LocalDate thisMonday = today.minusDays(today.getDayOfWeek().getValue() - 1);

		// 上周的星期一
		LocalDate lastMonday = thisMonday.minusWeeks(1);

		// 从上周一到上周日（共7天）
		for (int i = 0; i < 7; i++) {
			LocalDate date = lastMonday.plusDays(i);
			dates.add(date.format(DATE_FORMATTER));
		}
		return dates;
	}

}
