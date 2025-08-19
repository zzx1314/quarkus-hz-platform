package org.hzai.ai.aidocument.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.hzai.ai.aidocument.entity.AiDocument;
import org.hzai.ai.aidocument.entity.dto.AiDocumentQueryDto;
import org.hzai.ai.aidocument.repository.AiDocumentRepository;
import org.hzai.ai.aistatistics.util.DateUtil;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiDocumentServiceImp implements AiDocumentService {
    @Inject
    AiDocumentRepository repository;
    @Override
    public List<AiDocument> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiDocument> listEntitysByDto(AiDocumentQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiDocument> listPage(AiDocumentQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiDocument entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public List<Long> getDocumentCount() {
        List<Long> data = new ArrayList<>();
		List<Map<String, Object>> documentCountByDay = repository.getDocumentCountByDay();

		List<String> lastSevenDays = DateUtil.getLastSevenDays();

		if (!documentCountByDay.isEmpty()) {
			// 将数据库统计结果转为 Map<date, count>
			Map<String, Object> countMap = new HashMap<>();
			for (Map<String, Object> map : documentCountByDay) {
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
    public List<Long> getDocumentCountBefore() {
        List<Long> data = new ArrayList<>();
		List<Map<String, Object>> documentCountByDay = repository.getDocumentCountByDay();

		List<String> lastSevenDays = DateUtil.getLast14DaysToLast7Days();

		if (!documentCountByDay.isEmpty()) {
			// 将数据库统计结果转为 Map<date, count>
			Map<String, Object> countMap = new HashMap<>();
			for (Map<String, Object> map : documentCountByDay) {
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
    public List<Map<String, Object>> countDocumentsByKnowledgeBase() {
        return repository.countDocumentsByKnowledgeBase();
    }
}