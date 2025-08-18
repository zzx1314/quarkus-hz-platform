package org.hzai.ai.aiknowledgebase.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hzai.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseQueryDto;
import org.hzai.ai.aiknowledgebase.repository.AiKnowledgeBaseRepository;
import org.hzai.ai.aistatistics.util.DateUtil;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiKnowledgeBaseServiceImp implements AiKnowledgeBaseService {
    @Inject
    AiKnowledgeBaseRepository repository;
    @Override
    public List<AiKnowledgeBase> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiKnowledgeBase> listEntitysByDto(AiKnowledgeBaseQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiKnowledgeBase> listPage(AiKnowledgeBaseQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiKnowledgeBase entity) {
        repository.persist(entity);
        return true;
    }

    @Override
    public List<Long> getKnowledgeBaseCount() {
        List<Long> data = new ArrayList<>();
		List<Map<String, Object>> knowledgeBaseCountByDay = repository.getKnowledgeBaseCountByDay();

		// 获取最近7天的日期列表（格式：yyyy-MM-dd）
		List<String> lastSevenDays = DateUtil.getLastSevenDays();

		if (knowledgeBaseCountByDay != null && !knowledgeBaseCountByDay.isEmpty()) {
			// 将数据库统计结果转为 Map<date, count>
			Map<String, Object> countMap = new HashMap<>();
			for (Map<String, Object> map : knowledgeBaseCountByDay) {
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

}