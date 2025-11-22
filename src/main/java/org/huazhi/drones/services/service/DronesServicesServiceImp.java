package org.huazhi.drones.services.service;

import java.util.List;
import java.time.LocalDateTime;

import org.huazhi.drones.services.entity.DronesServices;
import org.huazhi.drones.services.entity.dto.DronesServicesQueryDto;
import org.huazhi.drones.services.entity.dto.DronesServicesDto;
import org.huazhi.drones.services.repository.DronesServicesRepository;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class DronesServicesServiceImp implements DronesServicesService {
    @Inject
    DronesServicesRepository repository;
    @Override
    public List<DronesServices> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<DronesServices> listEntitysByDto(DronesServicesQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesServices listOne(DronesServicesQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesServices> listPage(DronesServicesQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Transactional
    @Override
    public Boolean register(DronesServices entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(DronesServices entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesServicesDto dto) {
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