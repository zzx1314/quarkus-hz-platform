package org.huazhi.ai.aimodel.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.huazhi.ai.aimodel.entity.AiModel;
import org.huazhi.ai.aimodel.entity.dto.AiModelDto;
import org.huazhi.ai.aimodel.entity.dto.AiModelQueryDto;
import org.huazhi.ai.aimodel.entity.mapper.AiModelMapper;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.QueryBuilder;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AiModelRepository implements PanacheRepository<AiModel> {
    @Inject
    AiModelMapper mapper;

     public List<AiModel> selectList(AiModelQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("enable", queryDto.getEnable())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public AiModel selectOne(AiModelQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("modeType", queryDto.getModelType())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).singleResult();
    }

    public PageResult<AiModel> selectPage(AiModelQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<AiModel> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

    @Transactional
    public void updateById(AiModel dto) {
        AiModel entity = this.findById(dto.getId());
        mapper.updateEntity(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void updateByDto(AiModelDto dto) {
        AiModel entity = this.findById(dto.getId());
        mapper.updateEntityFromDto(dto, entity);
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