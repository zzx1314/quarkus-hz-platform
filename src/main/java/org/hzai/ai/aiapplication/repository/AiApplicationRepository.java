package org.hzai.ai.aiapplication.repository;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hzai.ai.aiapplication.entity.AiApplication;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationDto;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationQueryDto;
import org.hzai.ai.aiapplication.entity.mapper.AiApplicationMapper;
import org.hzai.ai.aiapplication.entity.vo.AiApplicationVo;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.QueryBuilder;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiApplicationRepository implements PanacheRepository<AiApplication> {
    @Inject
    AiApplicationMapper mapper;

    public List<AiApplication> selectList(AiApplicationQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public PageResult<AiApplicationVo> selectPage(AiApplicationQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .in("roles", dto.getRoleIdList())
                .equal("createId", dto.getCreateId())
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<AiApplication> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));
        List<AiApplication> list = query.list();
        List<AiApplicationVo> result = new ArrayList<>();
        for (AiApplication aiApplication : list) {
            AiApplicationVo aiApplicationVo = mapper.toVo(aiApplication);
            aiApplicationVo.setCreateUsername(aiApplication.getSysUser().getUsername());
            if (StringUtil.isNullOrEmpty(aiApplication.getKnowledgeIds())) {
                String[] kArray = aiApplication.getKnowledgeIds().split(",");
                aiApplicationVo.setKnowledgeIdList(Arrays.stream(kArray).map(Integer::parseInt).toList());
                aiApplicationVo.setKnowledgeCount(kArray.length);
            } else {
                aiApplicationVo.setKnowledgeCount(0);
            }
            if (StringUtil.isNullOrEmpty(aiApplication.getMcpIds())) {
                String[] aArray = aiApplication.getMcpIds().split(",");
                aiApplicationVo.setMcpIdList(Arrays.stream(aArray).map(Integer::parseInt).toList());
                aiApplicationVo.setMcpCount(aArray.length);
            } else {
                aiApplicationVo.setMcpCount(0);
            }
            if (!aiApplication.getRoles().isEmpty()) {
                aiApplicationVo.setRoleIdList(aiApplication.getRoles());
            }
            result.add(aiApplicationVo);
        }
        return new PageResult<>(
                result,
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

    public Map<Long, Map<String, Object>> getAppCountByKbIdList(List<Long> kbIdList) {
        if (kbIdList == null || kbIdList.isEmpty()) {
            return Collections.emptyMap();
        }

        // 动态拼接 VALUES
        String valuesSql = kbIdList.stream()
                .map(id -> "(" + id + ")")
                .collect(Collectors.joining(", "));

        String sql = """
                SELECT
                    items.item AS knowledgeId,
                    COUNT(*) AS count
                FROM (
                    SELECT
                        ai_application.id,
                        ai_application.knowledge_ids
                    FROM ai_application
                    WHERE ai_application.is_deleted = 0
                ) AS subquery,
                (VALUES %s) AS items(item)
                WHERE items.item::text = ANY(string_to_array(subquery.knowledge_ids, ','))
                GROUP BY items.item
                """.formatted(valuesSql);

        @SuppressWarnings("unchecked")
        List<Object[]> resultList = getEntityManager().createNativeQuery(sql).getResultList();

        // 转换成 Map<Long, Map<String, Object>>
        Map<Long, Map<String, Object>> result = new LinkedHashMap<>();
        for (Object[] row : resultList) {
            Long knowledgeId = ((Number) row[0]).longValue();
            Long count = ((Number) row[1]).longValue();

            Map<String, Object> data = new HashMap<>();
            data.put("knowledgeId", knowledgeId);
            data.put("count", count);

            result.put(knowledgeId, data);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getApplicationCountByDay() {
        String sql = """
                    SELECT
                        TO_CHAR(date_trunc('day', create_time), 'YYYY-MM-DD') AS date,
                        COUNT(*) AS count
                    FROM ai_application
                    WHERE create_time >= NOW() - INTERVAL '14 days'
                    GROUP BY date_trunc('day', create_time)
                    ORDER BY date ASC
                """;

        List<Object[]> rows = getEntityManager().createNativeQuery(sql).getResultList();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0]);
            map.put("count", ((Number) row[1]).longValue());
            result.add(map);
        }
        return result;
    }

    @Transient
    public void updateById(AiApplication dto) {
        AiApplication entity = this.findById(dto.getId());
        mapper.updateEntity(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transient
    public void updateByDto(AiApplicationDto aiApplication) {
        AiApplication entity = this.findById(aiApplication.getId());
        mapper.updateEntityFromDto(aiApplication, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

}