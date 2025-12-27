package org.huazhi.ai.aimcptools.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.huazhi.ai.aimcptools.entity.AiMcpTools;
import org.huazhi.ai.aimcptools.entity.dto.AiMcpToolsDto;
import org.huazhi.ai.aimcptools.entity.dto.AiMcpToolsQueryDto;
import org.huazhi.ai.aimcptools.entity.mapper.AiMcpToolsMapper;
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
public class AiMcpToolsRepository implements PanacheRepository<AiMcpTools> {
    @Inject
    AiMcpToolsMapper mapper;

     public List<AiMcpTools> selectList(AiMcpToolsQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("mcpId", queryDto.getMcpId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public AiMcpTools selectOne(AiMcpToolsQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).singleResult();
    }

    public PageResult<AiMcpTools> selectPage(AiMcpToolsQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<AiMcpTools> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

    @Transactional
    public void updateById(AiMcpTools dto) {
        AiMcpTools entity = this.findById(dto.getId());
        mapper.updateEntity(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void updateByDto(AiMcpToolsDto dto) {
        AiMcpTools entity = this.findById(dto.getId());
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

    @Transactional
    public void updateList(List<AiMcpTools> toolsToUpdate) {
        for (AiMcpTools tool : toolsToUpdate) {
            AiMcpTools entity = this.findById(tool.getId());
            if (entity != null) {
                mapper.updateEntity(tool, entity);
                entity.setUpdateTime(LocalDateTime.now());
            }
        }
    }

    public void insertBatch(List<AiMcpTools> toolsToInsert) {
        for (AiMcpTools tool : toolsToInsert) {
            tool.setCreateTime(LocalDateTime.now());
            tool.setUpdateTime(LocalDateTime.now());
            this.persist(tool);
        }
    }

}