package org.huazhi.ai.aiknowledgebase.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.hibernate.query.NativeQuery;
import org.huazhi.ai.aiapplication.repository.AiApplicationRepository;
import org.huazhi.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.huazhi.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseDto;
import org.huazhi.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseQueryDto;
import org.huazhi.ai.aiknowledgebase.entity.mapper.AiKnowledgeBaseMapper;
import org.huazhi.ai.aiknowledgebase.entity.vo.AiKnowledgeBaseVo;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.QueryBuilder;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AiKnowledgeBaseRepository implements PanacheRepository<AiKnowledgeBase> {
    @Inject
    AiApplicationRepository aiApplicationService;

    @Inject
    AiKnowledgeBaseMapper aiKnowledgeBaseMapper;

     public List<AiKnowledgeBase> selectList(AiKnowledgeBaseQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public AiKnowledgeBase selectOne(AiKnowledgeBaseQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).singleResult();
    }

    public PageResult<AiKnowledgeBaseVo> selectPage(AiKnowledgeBaseQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<AiKnowledgeBase> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        List<AiKnowledgeBase> records = query.list();
        List<AiKnowledgeBaseVo> result = new ArrayList<>();
        if (!records.isEmpty()) {
            List<Long> kbIdList = records.stream().map(AiKnowledgeBase::getId).toList();
            Map<Long, Map<String, Object>> appCountByKbIdList = aiApplicationService.getAppCountByKbIdList(kbIdList);
            for (AiKnowledgeBase aiKnowledgeBase : records) {
                AiKnowledgeBaseVo aiKnowledgeBaseVo = aiKnowledgeBaseMapper.toVo(aiKnowledgeBase);
                if (appCountByKbIdList != null && appCountByKbIdList.get(Long.valueOf(aiKnowledgeBase.getId().toString())) != null) {
                    Object count = appCountByKbIdList.get(Long.valueOf(aiKnowledgeBase.getId().toString())).get("count");
                    aiKnowledgeBaseVo.setAppCount(Long.valueOf(count.toString()));
                } else {
                    aiKnowledgeBaseVo.setAppCount(0L);
                }
                result.add(aiKnowledgeBaseVo);
            }
        }

        return new PageResult<>(
                result,
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getKnowledgeBaseCountByDay() {
        String sql = """
            SELECT 
                TO_CHAR(date_trunc('day', create_time), 'YYYY-MM-DD') AS date,
                COUNT(*) AS count
            FROM ai_knowledge_base
            WHERE create_time >= NOW() - INTERVAL '14 days'
            GROUP BY date_trunc('day', create_time)
            ORDER BY date ASC
        """;
        return getEntityManager()
        .createNativeQuery(sql, Object[].class)
        .unwrap(NativeQuery.class)
        .setTupleTransformer((tuple, aliases) -> {
            Map<String, Object> map = new LinkedHashMap<>();
            for (int i = 0; i < aliases.length; i++) {
                map.put(aliases[i], tuple[i]);
            }
            return map;
        })
        .getResultList();
    }

    @Transactional
    public void updateById(AiKnowledgeBase dto) {
        AiKnowledgeBase entity = this.findById(dto.getId());
        aiKnowledgeBaseMapper.updateEntity(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void updateByDto(AiKnowledgeBaseDto dto) {
        AiKnowledgeBase entity = this.findById(dto.getId());
        aiKnowledgeBaseMapper.updateEntityFromDto(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
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