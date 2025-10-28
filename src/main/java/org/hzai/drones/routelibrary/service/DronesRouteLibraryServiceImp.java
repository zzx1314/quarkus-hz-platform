package org.hzai.drones.routelibrary.service;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.drones.routelibrary.entity.DronesRouteLibrary;
import org.hzai.drones.routelibrary.entity.dto.DronesRouteLibraryQueryDto;
import org.hzai.drones.routelibrary.entity.dto.DronesRouteLibraryDto;
import org.hzai.drones.routelibrary.repository.DronesRouteLibraryRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DronesRouteLibraryServiceImp implements DronesRouteLibraryService {
    @Inject
    DronesRouteLibraryRepository repository;
    @Override
    public List<DronesRouteLibrary> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<DronesRouteLibrary> listEntitysByDto(DronesRouteLibraryQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesRouteLibrary listOne(DronesRouteLibraryQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesRouteLibrary> listPage(DronesRouteLibraryQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(DronesRouteLibrary entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setIsDeleted(0);
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(DronesRouteLibrary entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesRouteLibraryDto dto) {
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