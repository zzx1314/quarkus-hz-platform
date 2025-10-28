package org.hzai.drones.model.service;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.drones.model.entity.DronesModel;
import org.hzai.drones.model.entity.dto.DronesModelQueryDto;
import org.hzai.drones.model.entity.dto.DronesModelDto;
import org.hzai.drones.model.repository.DronesModelRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DronesModelServiceImp implements DronesModelService {
    @Inject
    DronesModelRepository repository;
    @Override
    public List<DronesModel> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<DronesModel> listEntitysByDto(DronesModelQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesModel listOne(DronesModelQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesModel> listPage(DronesModelQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(DronesModel entity) {
        entity.setIsDeleted(0);
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(DronesModel entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesModelDto dto) {
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