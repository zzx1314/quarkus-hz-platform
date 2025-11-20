package org.huazhi.drones.device.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.huazhi.drones.common.SelectOption;
import org.huazhi.drones.device.entity.DronesDevice;
import org.huazhi.drones.device.entity.dto.DronesDeviceDto;
import org.huazhi.drones.device.entity.dto.DronesDeviceQueryDto;
import org.huazhi.drones.device.repository.DronesDeviceRepository;
import org.huazhi.drones.util.DateUtil;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import java.time.LocalDateTime;

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
                .map(item -> new SelectOption(item.getDeviceId(), item.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getDeviceCount() {
        List<Long> data = new ArrayList<>();
        List<Map<String, Object>> deviceCountByDay = repository.getDeviceCountByDay();

        List<String> lastSevenDays = DateUtil.getLastSevenDays();

        if (!deviceCountByDay.isEmpty()) {
            // 将数据库统计结果转为 Map<date, count>
            Map<String, Object> countMap = new HashMap<>();
            for (Map<String, Object> map : deviceCountByDay) {
                countMap.put(map.get("date").toString(), map.get("count"));
            }
            // 按照最近7天顺序填充数据，缺失的日期设为0
            for (String date : lastSevenDays) {
                data.add(Long.valueOf(countMap.getOrDefault(date, 0L).toString()));
            }
        } else {
            // 如果没有数据，则全部填充0
            for (int i = 0; i < 7; i++) {
                data.add(0L);
            }
        }
        return data;
    }

    @Override
    public List<Long> getDeviceCountBefore() {
        List<Long> data = new ArrayList<>();
        List<Map<String, Object>> deviceCountByDay = repository.getDeviceCountByDay();

        List<String> lastSevenDays = DateUtil.getLast14DaysToLast7Days();

        if (!deviceCountByDay.isEmpty()) {
            // 将数据库统计结果转为 Map<date, count>
            Map<String, Object> countMap = new HashMap<>();
            for (Map<String, Object> map : deviceCountByDay) {
                countMap.put(map.get("date").toString(), map.get("count"));
            }
            // 按照最近7天顺序填充数据，缺失的日期设为0
            for (String date : lastSevenDays) {
                data.add(Long.valueOf(countMap.getOrDefault(date, 0L).toString()));
            }
        } else {
            // 如果没有数据，则全部填充0
            for (int i = 0; i < 7; i++) {
                data.add(0L);
            }
        }

        return data;
    }

    @Override
    public long count() {
       return repository.count("isDeleted = ?1", 0);
    }

    @Override
    public List<Map<String, Object>> statisticsDeviceTypeNumber() {
        return repository.countDeviceByType();
    }

    @Override
    public DronesDevice listById(Long id) {
        return repository.findById(id);
    }


}