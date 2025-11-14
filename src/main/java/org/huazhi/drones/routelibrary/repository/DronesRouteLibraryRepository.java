package org.huazhi.drones.routelibrary.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.huazhi.drones.routelibrary.entity.DronesRouteLibrary;
import org.huazhi.drones.routelibrary.entity.dto.DronesRouteLibraryDto;
import org.huazhi.drones.routelibrary.entity.dto.DronesRouteLibraryQueryDto;
import org.huazhi.drones.routelibrary.entity.mapper.DronesRouteLibraryMapper;
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
public class DronesRouteLibraryRepository implements PanacheRepository<DronesRouteLibrary> {
    @Inject
    DronesRouteLibraryMapper mapper;

     public List<DronesRouteLibrary> selectList(DronesRouteLibraryQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public DronesRouteLibrary selectOne(DronesRouteLibraryQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).singleResult();
    }

    public PageResult<DronesRouteLibrary> selectPage(DronesRouteLibraryQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<DronesRouteLibrary> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

    @Transactional
    public void updateById(DronesRouteLibrary dto) {
        DronesRouteLibrary entity = this.findById(dto.getId());
        mapper.updateEntity(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void updateByDto(DronesRouteLibraryDto dto) {
        DronesRouteLibrary entity = this.findById(dto.getId());
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

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDronesRouteLibraryCountByDay() {
        String sql = """
            SELECT
                TO_CHAR(date_trunc('day', create_time), 'YYYY-MM-DD') AS date,
                COUNT(*) AS count
            FROM drones_route_library
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

}