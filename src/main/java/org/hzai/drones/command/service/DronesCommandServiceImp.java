package org.hzai.drones.command.service;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.drones.command.entity.DronesCommand;
import org.hzai.drones.command.entity.dto.DronesCommandQueryDto;
import org.hzai.drones.command.entity.dto.DronesCommandDto;
import org.hzai.drones.command.repository.DronesCommandRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DronesCommandServiceImp implements DronesCommandService {
    @Inject
    DronesCommandRepository repository;
    @Override
    public List<DronesCommand> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<DronesCommand> listEntitysByDto(DronesCommandQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesCommand listOne(DronesCommandQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesCommand> listPage(DronesCommandQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(DronesCommand entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(DronesCommand entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesCommandDto dto) {
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