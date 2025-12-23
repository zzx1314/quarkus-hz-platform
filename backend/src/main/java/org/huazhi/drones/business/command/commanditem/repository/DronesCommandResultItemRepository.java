package org.huazhi.drones.business.command.commanditem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.huazhi.drones.business.command.commanditem.entity.DronesCommandResultItem;
import org.huazhi.drones.business.command.commanditem.entity.dto.DronesCommandResultItemDto;
import org.huazhi.drones.business.command.commanditem.entity.dto.DronesCommandResultItemQueryDto;
import org.huazhi.drones.business.command.commanditem.entity.mapper.DronesCommandResultItemMapper;
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
public class DronesCommandResultItemRepository implements PanacheRepository<DronesCommandResultItem> {
    @Inject
    DronesCommandResultItemMapper mapper;

    public List<DronesCommandResultItem> selectList(DronesCommandResultItemQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("commandId", queryDto.getCommandId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public DronesCommandResultItem selectOne(DronesCommandResultItemQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).singleResult();
    }

    public PageResult<DronesCommandResultItem> selectPage(DronesCommandResultItemQueryDto dto,
            PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<DronesCommandResultItem> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

    @Transactional
    public void updateById(DronesCommandResultItem dto) {
        DronesCommandResultItem entity = this.findById(dto.getId());
        mapper.updateEntity(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void updateByDto(DronesCommandResultItemDto dto) {
        DronesCommandResultItem entity = this.findById(dto.getId());
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