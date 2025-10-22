package org.hzai.drones.config.service;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.drones.config.entity.DronesConfig;
import org.hzai.drones.config.entity.dto.DronesConfigQueryDto;
import org.hzai.drones.config.entity.dto.DronesConfigDto;
import org.hzai.drones.config.repository.DronesConfigRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DronesConfigServiceImp implements DronesConfigService {
    @Inject
    DronesConfigRepository repository;
    @Override
    public List<DronesConfig> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<DronesConfig> listEntitysByDto(DronesConfigQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesConfig listOne(DronesConfigQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesConfig> listPage(DronesConfigQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(DronesConfig entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(DronesConfig entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesConfigDto dto) {
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