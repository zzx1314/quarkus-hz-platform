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

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@ApplicationScoped
public class AiApplicationRepository implements PanacheRepository<AiApplication> {
    @Inject
    AiApplicationMapper mapper;

    @PersistenceContext
    EntityManager em;

    public List<AiApplication> selectList(AiApplicationQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public PageResult<AiApplicationVo> selectPage(AiApplicationQueryDto dto, PageRequest pageRequest) {

        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .arrayOverlap("roles", dto.getRoleIdList())
                .equal("createId", dto.getCreateId())
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        String whereSql = qb.getQuery()
                .replace("isDeleted =", "is_deleted =")
                .replace("createId =", "create_id =")
                .replace("createTime", "create_time");

        String sql = """
                SELECT *
                FROM ai_application
                WHERE %s
                """.formatted(whereSql);

        Query nativeQuery = em.createNativeQuery(sql, AiApplication.class);

        // 参数名与 SQL 中完全一致（:isDeleted / :createId / :roles_arr）
        qb.getParams().forEach(nativeQuery::setParameter);

        nativeQuery.setFirstResult(pageRequest.getPageIndex() * pageRequest.getSize());
        nativeQuery.setMaxResults(pageRequest.getSize());

        @SuppressWarnings("unchecked")
        List<AiApplication> list = nativeQuery.getResultList();

        List<AiApplicationVo> result = new ArrayList<>();
        for (AiApplication aiApplication : list) {

            AiApplicationVo vo = mapper.toVo(aiApplication);
            vo.setCreateUsername(aiApplication.getSysUser().getUsername());

            // 修复 isNullOrEmpty 反逻辑问题
            if (!StringUtil.isNullOrEmpty(aiApplication.getKnowledgeIds())) {
                String[] kArray = aiApplication.getKnowledgeIds().split(",");
                vo.setKnowledgeIdList(Arrays.stream(kArray)
                        .map(Integer::parseInt)
                        .toList());
                vo.setKnowledgeCount(kArray.length);
            } else {
                vo.setKnowledgeCount(0);
            }

            if (!StringUtil.isNullOrEmpty(aiApplication.getMcpIds())) {
                String[] mArray = aiApplication.getMcpIds().split(",");
                vo.setMcpIdList(Arrays.stream(mArray)
                        .map(Integer::parseInt)
                        .toList());
                vo.setMcpCount(mArray.length);
            } else {
                vo.setMcpCount(0);
            }

            if (aiApplication.getRoles() != null && !aiApplication.getRoles().isEmpty()) {
                vo.setRoleIdList(aiApplication.getRoles());
            }

            result.add(vo);
        }

        /**
         * ===== COUNT SQL =====
         */
        String countSql = """
                SELECT COUNT(*)
                FROM ai_application
                WHERE %s
                """.formatted(
                // 去掉 order by
                whereSql.replaceAll("order by[\\s\\S]*$", ""));

        Query countQuery = em.createNativeQuery(countSql);

        //  复用同一套参数
        qb.getParams().forEach(countQuery::setParameter);

        long total = ((Number) countQuery.getSingleResult()).longValue();

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