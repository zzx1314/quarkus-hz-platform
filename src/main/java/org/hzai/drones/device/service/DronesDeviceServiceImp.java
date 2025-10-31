package org.hzai.drones.device.service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.hzai.ai.common.SelectOption;
import org.hzai.drones.device.entity.DronesDevice;
import org.hzai.drones.device.entity.dto.DronesDeviceQueryDto;
import org.hzai.drones.device.entity.dto.DronesDeviceDto;
import org.hzai.drones.device.repository.DronesDeviceRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DronesDeviceServiceImp implements DronesDeviceService {
    @Inject
    DronesDeviceRepository repository;
    @Override
    public List<DronesDevice> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<DronesDevice> listEntitysByDto(DronesDeviceQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesDevice listOne(DronesDeviceQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesDevice> listPage(DronesDeviceQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    @Transactional
    public Boolean register(DronesDevice entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(DronesDevice entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesDeviceDto dto) {
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
    public void replaceByQuery(DronesDeviceDto dto, DronesDeviceQueryDto queryDto) {
        repository.updateByQuery(dto, queryDto);
    }

    @Override
    public void registerByDto(DronesDeviceDto deviceDto) {
        repository.insertByDto(deviceDto);
    }

    @Override
    public List<SelectOption> getSelectOptions() {
        List<DronesDevice> listAll = repository.listAll();
        return listAll.stream()
                .map(item -> new SelectOption(item.getDeviceName(), item.getId()))
                .collect(Collectors.toList());
    }

}