package org.hzai.drones.device.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.hzai.drones.device.entity.DronesDevice;
import org.hzai.drones.device.entity.dto.DronesDeviceDto;
import org.hzai.drones.device.entity.dto.DronesDeviceQueryDto;
import org.hzai.drones.device.entity.mapper.DronesDeviceMapper;
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
        return find(qb.getQuery(), qb.getParams()).firstResult();
    }

    public PageResult<DronesDevice> selectPage(DronesDeviceQueryDto dto, PageRequest pageRequest) {
        QueryBuilder qb = QueryBuilder.create()
                .equal("isDeleted", 0)
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
            selectOne.setCommTime(LocalDateTime.now());
            selectOne.setStatus(dto.getStatus());
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

}