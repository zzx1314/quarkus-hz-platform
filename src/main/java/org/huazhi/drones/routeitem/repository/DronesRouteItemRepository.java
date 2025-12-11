package org.huazhi.drones.routeitem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.huazhi.drones.routeitem.entity.DronesRouteItem;
import org.huazhi.drones.routeitem.entity.dto.DronesRouteItemDto;
import org.huazhi.drones.routeitem.entity.dto.DronesRouteItemQueryDto;
import org.huazhi.drones.routeitem.entity.mapper.DronesRouteItemMapper;
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
public class DronesRouteItemRepository implements PanacheRepository<DronesRouteItem> {
    @Inject
    DronesRouteItemMapper mapper;

    public List<DronesRouteItem> selectList(DronesRouteItemQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("routeLibraryId", queryDto.getRouteLibraryId())
                .equal("isDeleted", 0)
                .orderBy("createTime asc");
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public DronesRouteItem selectOne(DronesRouteItemQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).singleResult();
    }

    public PageResult<DronesRouteItem> selectPage(DronesRouteItemQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<DronesRouteItem> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

    @Transactional
    public void updateById(DronesRouteItem dto) {
        DronesRouteItem entity = this.findById(dto.getId());
        mapper.updateEntity(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void updateByDto(DronesRouteItemDto dto) {
        DronesRouteItem entity = this.findById(dto.getId());
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

    public void deleteByRouteLibraryId(Long id) {
        this.delete("routeLibraryId =?1", id);
    }

}