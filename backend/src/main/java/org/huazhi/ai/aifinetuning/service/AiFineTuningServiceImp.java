package org.huazhi.ai.aifinetuning.service;

import java.util.List;

import org.huazhi.ai.aifinetuning.entity.AiFineTuning;
import org.huazhi.ai.aifinetuning.entity.dto.AiFineTuningDto;
import org.huazhi.ai.aifinetuning.entity.dto.AiFineTuningQueryDto;
import org.huazhi.ai.aifinetuning.repository.AiFineTuningRepository;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import java.time.LocalDateTime;

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
    public List<AiFineTuning> listEntitysByDto(AiFineTuningQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public AiFineTuning listOne(AiFineTuningQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<AiFineTuning> listPage(AiFineTuningQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiFineTuning entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setIsDeleted(0);
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(AiFineTuning entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(AiFineTuningDto dto) {
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

}