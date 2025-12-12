package org.huazhi.drones.routeitem.service;

import java.util.List;
import java.time.LocalDateTime;

import org.huazhi.drones.routeitem.entity.DronesRouteItem;
import org.huazhi.drones.routeitem.entity.dto.DronesRouteItemQueryDto;
import org.huazhi.drones.routeitem.entity.dto.DronesRouteItemDto;
import org.huazhi.drones.routeitem.repository.DronesRouteItemRepository;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DronesRouteItemServiceImp implements DronesRouteItemService {
    @Inject
    DronesRouteItemRepository repository;

    @Override
    public List<DronesRouteItem> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"), 0);
    }

    @Override
    public List<DronesRouteItem> listEntitysByDto(DronesRouteItemQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesRouteItem listOne(DronesRouteItemQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesRouteItem> listPage(DronesRouteItemQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Transactional
    @Override
    public Boolean register(DronesRouteItem entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(DronesRouteItem entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesRouteItemDto dto) {
        repository.updateByDto(dto);
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void removeByIds(List<Long> ids) {
        repository.deleteByIds(ids);
    }

    @Override
    public DronesRouteItem listById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void removeByRouteLibraryId(Long id) {
        repository.deleteByRouteLibraryId(id);
    }

}