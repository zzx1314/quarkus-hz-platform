package org.huazhi.drones.business.media.service;

import org.jboss.logging.Logger;

import java.util.List;
import java.time.LocalDateTime;

import org.huazhi.config.FileConfig;
import org.huazhi.drones.business.media.entity.DronesMedia;
import org.huazhi.drones.business.media.entity.dto.DronesMediaDto;
import org.huazhi.drones.business.media.entity.dto.DronesMediaQueryDto;
import org.huazhi.drones.business.media.repository.DronesMediaRepository;
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

@ApplicationScoped
public class DronesMediaServiceImp implements DronesMediaService {
    private static final Logger log = Logger.getLogger(DronesMediaServiceImp.class);
    
    @Inject
    DronesMediaRepository repository;

    @Inject
    FileConfig fileConfig;

    @Inject
    SecurityUtil securityUtil;

    @Override
    public List<DronesMedia> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"), 0);
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
    public Long register(DronesMedia entity) {
        entity.setIsDeleted(0);
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return entity.getId();
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
    public R<Long> uploadFile(FileUpload file, DronesMediaDto mediaDto) {
        log.info("Uploading file: " + file.fileName());
        // Save the file to a designated path
        String mcpFilePath = fileConfig.basePath() + "/media/" + securityUtil.getUserId();
        FileUtil.saveFile(file, mcpFilePath);
        if (mediaDto.getId() != null) {
            // Update existing record
            this.replaceByDto(mediaDto);
            log.info("Updating media record: " + mediaDto.getId());
            return R.ok(mediaDto.getId());
        } else {
            // Create new record
            DronesMedia dronesMedia = new DronesMedia();
            dronesMedia.setMediaName(file.fileName());
            dronesMedia.setMediaPath(mcpFilePath + "/" + file.fileName());
            dronesMedia.setMediaType(mediaDto.getMediaType());
            dronesMedia.setMediaSize(file.size());
            dronesMedia.setRemarks(mediaDto.getRemarks());
            this.register(dronesMedia);
            log.info("Creating new media record: " + dronesMedia.getId());
            return R.ok(dronesMedia.getId());
        }
    }

}