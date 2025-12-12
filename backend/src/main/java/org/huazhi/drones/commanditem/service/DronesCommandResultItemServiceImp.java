package org.huazhi.drones.commanditem.service;

import java.util.List;
import java.time.LocalDateTime;

import org.huazhi.drones.commanditem.entity.DronesCommandResultItem;
import org.huazhi.drones.commanditem.entity.dto.DronesCommandResultItemQueryDto;
import org.huazhi.drones.commanditem.entity.dto.DronesCommandResultItemDto;
import org.huazhi.drones.commanditem.repository.DronesCommandResultItemRepository;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DronesCommandResultItemServiceImp implements DronesCommandResultItemService {
    @Inject
    DronesCommandResultItemRepository repository;

    @Override
    public List<DronesCommandResultItem> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"), 0);
    }

    @Override
    public List<DronesCommandResultItem> listEntitysByDto(DronesCommandResultItemQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesCommandResultItem listOne(DronesCommandResultItemQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesCommandResultItem> listPage(DronesCommandResultItemQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Transactional
    @Override
    public Boolean register(DronesCommandResultItem entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(DronesCommandResultItem entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesCommandResultItemDto dto) {
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

}