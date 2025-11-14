package org.hzai.drones.routelibrary.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.hzai.ai.aistatistics.util.DateUtil;
import org.hzai.ai.common.SelectOption;
import org.hzai.drones.model.entity.DronesModel;
import org.hzai.drones.model.entity.dto.DronesModelQueryDto;
import org.hzai.drones.model.service.DronesModelService;
import org.hzai.drones.routelibrary.entity.DronesRouteLibrary;
import org.hzai.drones.routelibrary.entity.dto.DronesRouteLibraryQueryDto;
import org.hzai.drones.routelibrary.entity.dto.DronesRouteLibraryDto;
import org.hzai.drones.routelibrary.repository.DronesRouteLibraryRepository;
import org.hzai.util.ImageUtil;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.R;
import org.hzai.util.ZipUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DronesRouteLibraryServiceImp implements DronesRouteLibraryService {
    @Inject
    DronesRouteLibraryRepository repository;

    @Inject
    DronesModelService modelService;

    @Override
    public List<DronesRouteLibrary> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"), 0);
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
        entity.setRouteStatus("停用");
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

    @Override
    public Object getRoute(Long modelId) {
        // Get route from modelId
        DronesModel model = modelService.listOne(new DronesModelQueryDto().setId(modelId));
        // Unzip the model file, convert the format, and return a base64-encoded string.
        String modelFilePath = model.getFilePath();
        String unZipPath = "/temp/unzipped_model_" + modelId;
        ZipUtil.unzip(Paths.get(modelFilePath), Paths.get(unZipPath));
        // Find the pgm file and convert it to png format
        ImageUtil.pgmToPng(unZipPath + "/model.pgm", unZipPath + "/model.png");
        String base64Str = ImageUtil.getPngBase64String(unZipPath + "/model.png");
        if (base64Str != null) {
            return base64Str;
        }
        return "";
    }

    @Override
    public List<SelectOption> getSelectOptions() {
        List<DronesRouteLibrary> listAll = repository.listAll();
        return listAll.stream()
                .map(item -> new SelectOption(item.getRouteName(), item.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getRouteCount() {
        List<Long> data = new ArrayList<>();
        List<Map<String, Object>> modelCountByDay = repository.getDronesRouteLibraryCountByDay();

        List<String> lastSevenDays = DateUtil.getLastSevenDays();

        if (!modelCountByDay.isEmpty()) {
            // 将数据库统计结果转为 Map<date, count>
            Map<String, Object> countMap = new HashMap<>();
            for (Map<String, Object> map : modelCountByDay) {
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
    public List<Long> getRouteCountBefore() {
        List<Long> data = new ArrayList<>();
        List<Map<String, Object>> modelCountByDay = repository.getDronesRouteLibraryCountByDay();

        List<String> lastSevenDays = DateUtil.getLast14DaysToLast7Days();

        if (!modelCountByDay.isEmpty()) {
            // 将数据库统计结果转为 Map<date, count>
            Map<String, Object> countMap = new HashMap<>();
            for (Map<String, Object> map : modelCountByDay) {
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
        return repository.count();
    }

    @Override
    public void saveRouteData(DronesRouteLibraryDto data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<List<Double>> result = mapper.readValue(data.getRouteData(), mapper.getTypeFactory()
                    .constructCollectionType(List.class, mapper.getTypeFactory()
                            .constructCollectionType(List.class, Double.class)));
            data.setStartCoordinates(mapper.writeValueAsString(result.get(0)));
            data.setEndCoordinates(mapper.writeValueAsString(result.get(result.size() - 1)));
            repository.updateByDto(data);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public R<Object> startOrRoute(Long id, String status) {
        if ("start".equals(status)) {
            // 启动,检查是否有航线数据
            DronesRouteLibrary route = repository.findById(id);
            if (route.getRouteData() == null || route.getRouteData().isEmpty()) {
                return R.failed("请对航线进行设计并保存");
            }
        }
        DronesRouteLibraryDto dto = new DronesRouteLibraryDto();
        dto.setId(id);
        dto.setRouteStatus(status.equals("start") ? "启用" : "停用");
        repository.updateByDto(dto);
        return R.ok();
    }

    @Override
    public List<SelectOption> getRouteOption(Long id) {
        // 获取所有航点
        List<SelectOption> routePoints = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        DronesRouteLibrary route = repository.findById(id);
        try {
            List<List<Double>> result = mapper.readValue(route.getRouteData(), mapper.getTypeFactory()
                    .constructCollectionType(List.class, mapper.getTypeFactory()
                            .constructCollectionType(List.class, Double.class)));
            for (int i = 0; i < result.size(); i++) {
                List<Double> point = result.get(i);
                routePoints.add(new SelectOption("航点" + (i + 1), point.get(0) + "," + point.get(1)));
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return routePoints;
    }

}