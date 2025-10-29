package org.hzai.drones.model.service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.hzai.ai.common.SelectOption;
import org.hzai.config.FileConfig;
import org.hzai.drones.model.entity.DronesModel;
import org.hzai.drones.model.entity.dto.DronesModelQueryDto;
import org.hzai.drones.model.entity.dto.DronesModelDto;
import org.hzai.drones.model.repository.DronesModelRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.R;
import org.hzai.util.SecurityUtil;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import org.hzai.util.FileUtil;
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
                .map(model -> new SelectOption(model.getFileName(), model.getId()))
                .collect(Collectors.toList());
    }

}