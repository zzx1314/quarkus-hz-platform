package org.hzai.drones.task.service;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.drones.task.entity.DronesTask;
import org.hzai.drones.task.entity.dto.DronesTaskQueryDto;
import org.hzai.drones.task.entity.dto.DronesTaskDto;
import org.hzai.drones.task.repository.DronesTaskRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DronesTaskServiceImp implements DronesTaskService {
    @Inject
    DronesTaskRepository repository;
    @Override
    public List<DronesTask> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<DronesTask> listEntitysByDto(DronesTaskQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesTask listOne(DronesTaskQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesTask> listPage(DronesTaskQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(DronesTask entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(DronesTask entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesTaskDto dto) {
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