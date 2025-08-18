package org.hzai.ai.aimodel.service;

import java.util.List;

import org.hzai.ai.aimodel.entity.AiModel;
import org.hzai.ai.aimodel.entity.dto.AiModelQueryDto;
import org.hzai.ai.aimodel.repository.AiModelRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiModelServiceImp implements AiModelService {
    @Inject
    AiModelRepository repository;
    @Override
    public List<AiModel> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiModel> listEntitysByDto(AiModelQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiModel> listPage(AiModelQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiModel entity) {
        repository.persist(entity);
        return true;
    }

}