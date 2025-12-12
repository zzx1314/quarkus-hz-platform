package org.huazhi.drones.model.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.huazhi.drones.model.entity.DronesModel;
import org.huazhi.drones.model.entity.dto.DronesModelDto;
import org.huazhi.drones.model.entity.dto.DronesModelQueryDto;
import org.huazhi.drones.model.entity.mapper.DronesModelMapper;
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
public class DronesModelRepository implements PanacheRepository<DronesModel> {
    @Inject
    DronesModelMapper mapper;

    public List<DronesModel> selectList(DronesModelQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public DronesModel selectOne(DronesModelQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("id", queryDto.getId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).singleResult();
    }

    public PageResult<DronesModel> selectPage(DronesModelQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .like("modelName", dto.getModelName())
                .like("modelType", dto.getModelType())
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<DronesModel> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

    @Transactional
    public void updateById(DronesModel dto) {
        DronesModel entity = this.findById(dto.getId());
        mapper.updateEntity(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void updateByDto(DronesModelDto dto) {
        DronesModel entity = this.findById(dto.getId());
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
    public List<Map<String, Object>> getModelCountByDay() {
        String sql = """
                    SELECT
                        TO_CHAR(date_trunc('day', create_time), 'YYYY-MM-DD') AS date,
                        COUNT(*) AS count
                    FROM drones_model
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