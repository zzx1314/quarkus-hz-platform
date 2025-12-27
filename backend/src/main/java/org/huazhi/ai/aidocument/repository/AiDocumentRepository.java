package org.huazhi.ai.aidocument.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.huazhi.ai.aidocument.entity.AiDocument;
import org.huazhi.ai.aidocument.entity.dto.AiDocumentQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.QueryBuilder;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AiDocumentRepository implements PanacheRepository<AiDocument> {

     public List<AiDocument> selectList(AiDocumentQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("knowledgeId", queryDto.getKnowledgeId())
                .equal("docName", queryDto.getFileName())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public PageResult<AiDocument> selectPage(AiDocumentQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<AiDocument> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDocumentCountByDay() {
        String sql = """
            SELECT
                TO_CHAR(date_trunc('day', create_time), 'YYYY-MM-DD') AS date,
                COUNT(*) AS count
            FROM ai_document
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
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> countDocumentsByKnowledgeBase() {
        String sql = """
            SELECT
                ai_knowledge_base.knowledge_base_name AS name,
                COUNT(ai_document.id) AS count
            FROM ai_document
            LEFT JOIN ai_knowledge_base 
                ON ai_document.knowledge_id = ai_knowledge_base.id
            WHERE ai_document.is_deleted = 0
            GROUP BY ai_document.knowledge_id, ai_knowledge_base.knowledge_base_name
            ORDER BY count DESC
        """;

        List<Object[]> rows = getEntityManager().createNativeQuery(sql).getResultList();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", row[0]);
            map.put("count", ((Number) row[1]).longValue());
            result.add(map);
        }

        return result;
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            for (Long id : ids) {
                this.deleteById(id);
            }
        }
    }

}