package org.hzai.ai.aiparagraph.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hzai.ai.aiparagraph.entity.AiParagraph;
import org.hzai.ai.aiparagraph.entity.dto.AiParagraphDto;
import org.hzai.ai.aiparagraph.entity.dto.AiParagraphQueryDto;
import org.hzai.ai.aiparagraph.entity.mapper.AiParagraphMapper;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.QueryBuilder;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AiParagraphRepository implements PanacheRepository<AiParagraph> {
    @Inject
    AiParagraphMapper aiParagraphMapper;

    public List<AiParagraph> selectList(AiParagraphQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("docId", queryDto.getDocId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public AiParagraph selectOne(AiParagraphQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("docId", queryDto.getDocId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).singleResult();
    }

    public PageResult<AiParagraph> selectPage(AiParagraphQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<AiParagraph> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
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

        return getEntityManager()
                .createNativeQuery(sql)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setTupleTransformer((tuple, aliases) -> {
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 0; i < aliases.length; i++) {
                        map.put(aliases[i], tuple[i]);
                    }
                    return map;
                })
                .getResultList();
    }

    @Transactional
    public void insertList(List<AiParagraph> lists) {
        for (AiParagraph entity : lists) {
            this.persist(entity);
        }
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            for (Long id : ids) {
                this.deleteById(id);
            }
        }
    }

    @Transactional
    public void deleteByDocumentId(Long id) {
        this.delete("documentId", id);
    }

    @Transactional
    public void updateById(AiParagraph aiParagraph) {
        AiParagraph entity = this.findById(aiParagraph.getId());
        aiParagraphMapper.updateEntity(aiParagraph, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void updateByDto(AiParagraphDto dto) {
        AiParagraph entity = this.findById(dto.getId());
        aiParagraphMapper.updateEntityFromDto(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

}