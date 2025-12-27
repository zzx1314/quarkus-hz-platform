package org.huazhi.ai.aiapplication.repository;

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

import org.huazhi.ai.aiapplication.entity.AiApplication;
import org.huazhi.ai.aiapplication.entity.dto.AiApplicationDto;
import org.huazhi.ai.aiapplication.entity.dto.AiApplicationQueryDto;
import org.huazhi.ai.aiapplication.entity.mapper.AiApplicationMapper;
import org.huazhi.ai.aiapplication.entity.vo.AiApplicationVo;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.QueryBuilder;

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

    public PageResult<AiApplicationVo> selectPage(
            AiApplicationQueryDto dto,
            PageRequest pageRequest) {

        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("isDeleted = 0 ");

        // createId
        if (dto.getCreateId() != null) {
            jpql.append("and createId = :createId ");
            params.put("createId", dto.getCreateId());
        }

        // roles（等价于 roles && roleIdList）
        if (dto.getRoleIdList() != null && !dto.getRoleIdList().isEmpty()) {
            jpql.append("and (");
            for (int i = 0; i < dto.getRoleIdList().size(); i++) {
                String key = "role" + i;
                if (i > 0) {
                    jpql.append(" or ");
                }
                jpql.append(":").append(key).append(" member of roles");
                params.put(key, dto.getRoleIdList().get(i));
            }
            jpql.append(") ");
        }

        // 时间范围
        if (dto.getBeginTime() != null && dto.getEndTime() != null) {
            jpql.append("and createTime between :beginTime and :endTime ");
            params.put("beginTime", dto.getBeginTime());
            params.put("endTime", dto.getEndTime());
        }

        jpql.append("order by createTime desc");

        PanacheQuery<AiApplication> query = AiApplication.find(jpql.toString(), params);

        // 分页
        query.page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        List<AiApplication> list = query.list();
        long total = query.count();

        // ===== VO 转换 =====
        List<AiApplicationVo> result = new ArrayList<>();
        for (AiApplication aiApplication : list) {

            AiApplicationVo vo = mapper.toVo(aiApplication);
            vo.setCreateUsername(aiApplication.getSysUser().getUsername());

            if (!StringUtil.isNullOrEmpty(aiApplication.getKnowledgeIds())) {
                String[] kArray = aiApplication.getKnowledgeIds().split(",");
                vo.setKnowledgeIdList(
                        Arrays.stream(kArray).map(Integer::parseInt).toList());
                vo.setKnowledgeCount(kArray.length);
            } else {
                vo.setKnowledgeCount(0);
            }

            if (!StringUtil.isNullOrEmpty(aiApplication.getMcpIds())) {
                String[] mArray = aiApplication.getMcpIds().split(",");
                vo.setMcpIdList(
                        Arrays.stream(mArray).map(Integer::parseInt).toList());
                vo.setMcpCount(mArray.length);
            } else {
                vo.setMcpCount(0);
            }

            if (aiApplication.getRoles() != null) {
                vo.setRoleIdList(aiApplication.getRoles());
            }

            result.add(vo);
        }

        return new PageResult<>(
                result,
                total,
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