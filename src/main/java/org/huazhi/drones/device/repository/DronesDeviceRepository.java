package org.huazhi.drones.device.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.huazhi.drones.device.entity.DronesDevice;
import org.huazhi.drones.device.entity.dto.DronesDeviceDto;
import org.huazhi.drones.device.entity.dto.DronesDeviceQueryDto;
import org.huazhi.drones.device.entity.mapper.DronesDeviceMapper;
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
public class DronesDeviceRepository implements PanacheRepository<DronesDevice> {
    @Inject
    DronesDeviceMapper mapper;

     public List<DronesDevice> selectList(DronesDeviceQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).list();
    }

    public DronesDevice selectOne(DronesDeviceQueryDto queryDto) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("deviceId", queryDto.getDeviceId())
                .equal("id", queryDto.getId())
                .equal("isDeleted", 0);
        return find(qb.getQuery(), qb.getParams()).singleResultOptional().orElse(null);
    }

    public PageResult<DronesDevice> selectPage(DronesDeviceQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
                .like("deviceId", dto.getDeviceId())
                .like("deviceType", dto.getDeviceType())
                .like("status", dto.getStatus())
                .between("createTime", dto.getBeginTime(), dto.getEndTime())
                .orderBy("createTime desc");

        PanacheQuery<DronesDevice> query = find(qb.getQuery(), qb.getParams())
                .page(Page.of(pageRequest.getPageIndex(), pageRequest.getSize()));

        return new PageResult<>(
                query.list(),
                query.count(),
                pageRequest.getPage(),
                pageRequest.getSize());
    }

    @Transactional
    public void updateById(DronesDevice dto) {
        DronesDevice entity = this.findById(dto.getId());
        mapper.updateEntity(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void updateByDto(DronesDeviceDto dto) {
        DronesDevice entity = this.findById(dto.getId());
        mapper.updateEntityFromDto(dto, entity);
        entity.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public void updateByQuery(DronesDeviceDto dto, DronesDeviceQueryDto queryDto) {
        DronesDevice selectOne = this.selectOne(queryDto);
        if (selectOne != null) {
            mapper.updateEntityFromDto(dto, selectOne);
            selectOne.setCommTime(LocalDateTime.now());
            selectOne.setUpdateTime(LocalDateTime.now());
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
    public void insertByDto(DronesDeviceDto deviceDto) {
        DronesDevice entity = new DronesDevice();
        mapper.updateEntityFromDto(deviceDto, entity);
        entity.setCreateTime(LocalDateTime.now());
        entity.setIsDeleted(0);
        this.persist(entity);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDeviceCountByDay() {
        String sql = """
            SELECT
                TO_CHAR(date_trunc('day', create_time), 'YYYY-MM-DD') AS date,
                COUNT(*) AS count
            FROM drones_device
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

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> countDeviceByType() {
        String sql = """
            SELECT
                drones_device.device_type AS name,
                COUNT(drones_device.id) AS count
            FROM drones_device
            WHERE drones_device.is_deleted = 0
            GROUP BY drones_device.device_type
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

}