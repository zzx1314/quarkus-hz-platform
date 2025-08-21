package org.hzai.ai.aiapplication.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hzai.ai.aiapplication.entity.AiApplication;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationQueryDto;
import org.hzai.ai.aiapplication.repository.AiApplicationRepository;
import org.hzai.ai.aistatistics.util.DateUtil;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiApplicationServiceImp implements AiApplicationService {
    @Inject
    AiApplicationRepository repository;
    @Override
    public List<AiApplication> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiApplication> listEntitysByDto(AiApplicationQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiApplication> listPage(AiApplicationQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiApplication entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public List<Long> getApplicationCount() {
         List<Long> data = new ArrayList<>();
		List<Map<String, Object>> applicationCountByDay = repository.getApplicationCountByDay();

		List<String> lastSevenDays = DateUtil.getLastSevenDays();

		if (applicationCountByDay != null  && !applicationCountByDay.isEmpty()) {
			// 将数据库统计结果转为 Map<date, count>
			Map<String, Object> countMap = new HashMap<>();
			for (Map<String, Object> map : applicationCountByDay) {
				countMap.put(map.get("date").toString(), map.get("count"));
			}
			// 按照最近7天顺序填充数据，缺失的日期设为0
			for (String date : lastSevenDays) {
				data.add(Long.valueOf(countMap.getOrDefault(date, 0L).toString()));
			}
		} else {
			// 如果没有数据，则全部填充0
			for (int i = 0; i < 7; i++) {
				data.add(0L);
			}
		}
		return data;
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public List<Long> getApplicationCountBefore() {
        List<Long> data = new ArrayList<>();
		List<Map<String, Object>> applicationCountByDay = repository.getApplicationCountByDay();

		List<String> lastSevenDays = DateUtil.getLast14DaysToLast7Days();

		if (!applicationCountByDay.isEmpty()) {
			// 将数据库统计结果转为 Map<date, count>
			Map<String, Object> countMap = new HashMap<>();
			for (Map<String, Object> map : applicationCountByDay) {
				countMap.put(map.get("date").toString(), map.get("count"));
			}
			// 按照最近7天顺序填充数据，缺失的日期设为0
			for (String date : lastSevenDays) {
				data.add(Long.valueOf(countMap.getOrDefault(date, 0L).toString()));
			}
		} else {
			// 如果没有数据，则全部填充0
			for (int i = 0; i < 7; i++) {
				data.add(0L);
			}
		}
		return data;
    }

}