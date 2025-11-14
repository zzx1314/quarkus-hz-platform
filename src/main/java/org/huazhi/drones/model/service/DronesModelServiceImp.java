package org.huazhi.drones.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.huazhi.config.FileConfig;
import org.huazhi.drones.common.SelectOption;
import org.huazhi.drones.model.entity.DronesModel;
import org.huazhi.drones.model.entity.dto.DronesModelDto;
import org.huazhi.drones.model.entity.dto.DronesModelQueryDto;
import org.huazhi.drones.model.repository.DronesModelRepository;
import org.huazhi.drones.util.DateUtil;
import org.huazhi.util.FileUtil;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;
import org.huazhi.util.SecurityUtil;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class DronesModelServiceImp implements DronesModelService {
    @Inject
    DronesModelRepository repository;

    @Inject
    SecurityUtil securityUtil;

    @Inject
    FileConfig fileConfig;

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
    @Transactional
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

    @Override
    public R<Object> uploadFile(FileUpload file, DronesModelDto modelDto) {
        log.info("Uploading file: {}", file.fileName());
        // Save the file to a designated path
        String mcpFilePath = fileConfig.basePath() + "/model/" + securityUtil.getUserId();
        FileUtil.saveFile(file, mcpFilePath);
        if (modelDto.getId() != null) {
            // Update existing record
            this.replaceByDto(modelDto);
        } else{
            // Create new record
            DronesModel dronesModel = new DronesModel();
            dronesModel.setModelName(modelDto.getModelName());
            dronesModel.setModelType(modelDto.getModelType());
            dronesModel.setFileName(file.fileName());
            dronesModel.setFilePath(mcpFilePath + "/" + file.fileName());
            dronesModel.setFileFormat(modelDto.getFileFormat());
            dronesModel.setFileSize(file.size());
            dronesModel.setRemarks(modelDto.getRemarks());
            this.register(dronesModel);
        }
        return R.ok();
    }

    @Override
    public List<SelectOption> getSelectOption() {
        // Query model becomes drop-down option
        List<DronesModel> models = this.listEntitys();
        return models.stream()
                .map(model -> new SelectOption(model.getModelName(), model.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getModelCount() {
        List<Long> data = new ArrayList<>();
        List<Map<String, Object>> modelCountByDay = repository.getModelCountByDay();

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
    public List<Long> getModelCountBefore() {
        List<Long> data = new ArrayList<>();
        List<Map<String, Object>> modelCountByDay = repository.getModelCountByDay();

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

}