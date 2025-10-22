package org.hzai.drones.media.service;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.drones.media.entity.DronesMedia;
import org.hzai.drones.media.entity.dto.DronesMediaQueryDto;
import org.hzai.drones.media.entity.dto.DronesMediaDto;
import org.hzai.drones.media.repository.DronesMediaRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DronesMediaServiceImp implements DronesMediaService {
    @Inject
    DronesMediaRepository repository;
    @Override
    public List<DronesMedia> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<DronesMedia> listEntitysByDto(DronesMediaQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesMedia listOne(DronesMediaQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesMedia> listPage(DronesMediaQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(DronesMedia entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(DronesMedia entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesMediaDto dto) {
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