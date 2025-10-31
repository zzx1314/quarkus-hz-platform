package org.hzai.drones.media.service;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.config.FileConfig;
import org.hzai.drones.media.entity.DronesMedia;
import org.hzai.drones.media.entity.dto.DronesMediaQueryDto;
import org.hzai.drones.media.entity.dto.DronesMediaDto;
import org.hzai.drones.media.repository.DronesMediaRepository;
import org.hzai.util.FileUtil;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.R;
import org.hzai.util.SecurityUtil;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class DronesMediaServiceImp implements DronesMediaService {
    @Inject
    DronesMediaRepository repository;

    @Inject
    FileConfig fileConfig;

    @Inject
    SecurityUtil securityUtil;
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
    @Transactional
    public Boolean register(DronesMedia entity) {
        entity.setIsDeleted(0);
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

    @Override
    public R<Object> uploadFile(FileUpload file, DronesMediaDto mediaDto) {
         log.info("Uploading file: {}", file.fileName());
        // Save the file to a designated path
        String mcpFilePath = fileConfig.basePath() + "/media/" + securityUtil.getUserId();
        FileUtil.saveFile(file, mcpFilePath);
        if (mediaDto.getId() != null) {
            // Update existing record
            this.replaceByDto(mediaDto);
        } else{
            // Create new record
            DronesMedia dronesMedia = new DronesMedia();
            dronesMedia.setMediaName(file.fileName());
            dronesMedia.setMediaPath(mcpFilePath + "/" + file.fileName());
            dronesMedia.setMediaType(mediaDto.getMediaType());
            dronesMedia.setMediaSize(file.size());
            dronesMedia.setRemarks(mediaDto.getRemarks());
            this.register(dronesMedia);
        }
        return R.ok();
    }

}