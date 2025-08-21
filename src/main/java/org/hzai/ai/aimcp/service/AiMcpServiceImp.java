package org.hzai.ai.aimcp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hzai.ai.aimcp.entity.AiMcp;
import org.hzai.ai.aimcp.entity.dto.AiMcpDto;
import org.hzai.ai.aimcp.entity.dto.AiMcpQueryDto;
import org.hzai.ai.aimcp.repository.AiMcpRepository;
import org.hzai.ai.aistatistics.util.DateUtil;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiMcpServiceImp implements AiMcpService {
    @Inject
    AiMcpRepository repository;
    @Override
    public List<AiMcp> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiMcp> listEntitysByDto(AiMcpQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiMcp> listPage(AiMcpQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiMcp entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public List<Long> getMcpCount() {
         List<Long> data = new ArrayList<>();
		List<Map<String, Object>> mcpCountByDay = repository.getMcpCountByDay();

		List<String> lastSevenDays = DateUtil.getLastSevenDays();

		if (!mcpCountByDay.isEmpty()) {
			// 将数据库统计结果转为 Map<date, count>
			Map<String, Object> countMap = new HashMap<>();
			for (Map<String, Object> map : mcpCountByDay) {
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
    public void replaceByDto(AiMcpDto dto) {
        repository.updateById(dto);
    }

    @Override
    public List<Long> getMcpCountBefore() {
         List<Long> data = new ArrayList<>();
		List<Map<String, Object>> mcpCountByDay = repository.getMcpCountByDay();

		List<String> lastSevenDays = DateUtil.getLast14DaysToLast7Days();

		if (!mcpCountByDay.isEmpty()) {
			// 将数据库统计结果转为 Map<date, count>
			Map<String, Object> countMap = new HashMap<>();
			for (Map<String, Object> map : mcpCountByDay) {
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