package org.huazhi.drones.command.service;

import java.util.List;

import org.huazhi.drones.command.entity.DronesCommand;
import org.huazhi.drones.command.entity.dto.DronesCommandDto;
import org.huazhi.drones.command.entity.dto.DronesCommandQueryDto;
import org.huazhi.drones.command.repository.DronesCommandRepository;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import java.time.LocalDateTime;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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
    @Transactional
    public Boolean register(DronesCommand entity) {
        entity.setIsDeleted(0);
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