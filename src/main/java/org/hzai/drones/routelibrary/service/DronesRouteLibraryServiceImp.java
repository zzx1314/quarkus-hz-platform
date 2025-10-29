package org.hzai.drones.routelibrary.service;

import java.util.List;
import java.nio.file.Paths;
import java.time.LocalDateTime;

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
import org.hzai.util.ZipUtil;

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

}