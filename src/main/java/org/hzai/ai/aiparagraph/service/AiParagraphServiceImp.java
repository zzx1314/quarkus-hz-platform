package org.hzai.ai.aiparagraph.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hzai.ai.aiparagraph.entity.AiParagraph;
import org.hzai.ai.aiparagraph.entity.dto.AiParagraphQueryDto;
import org.hzai.ai.aiparagraph.repository.AiParagraphRepository;
import org.hzai.ai.aistatistics.util.DateUtil;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiParagraphServiceImp implements AiParagraphService {
    @Inject
    AiParagraphRepository repository;
    @Override
    public List<AiParagraph> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiParagraph> listEntitysByDto(AiParagraphQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiParagraph> listPage(AiParagraphQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiParagraph entity) {
        repository.persist(entity);
        return true;
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