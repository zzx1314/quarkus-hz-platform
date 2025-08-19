package org.hzai.ai.aifinetuning.service;

import java.time.LocalDateTime;
import java.util.List;

import org.hzai.ai.aifinetuning.entity.AiFineTuning;
import org.hzai.ai.aifinetuning.entity.dto.AiFineTuningQueryDto;
import org.hzai.ai.aifinetuning.repository.AiFineTuningRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiFineTuningServiceImp implements AiFineTuningService {
    @Inject
    AiFineTuningRepository repository;
    @Override
    public List<AiFineTuning> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiFineTuning> listEntitysByDto(AiFineTuningQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiFineTuning> listPage(AiFineTuningQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiFineTuning entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

}