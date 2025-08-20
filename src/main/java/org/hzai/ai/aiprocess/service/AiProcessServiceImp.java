package org.hzai.ai.aiprocess.service;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.ai.aiprocess.entity.AiProcess;
import org.hzai.ai.aiprocess.entity.dto.AiProcessQueryDto;
import org.hzai.ai.aiprocess.entity.dto.AiProcessDto;
import org.hzai.ai.aiprocess.repository.AiProcessRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiProcessServiceImp implements AiProcessService {
    @Inject
    AiProcessRepository repository;
    @Override
    public List<AiProcess> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiProcess> listEntitysByDto(AiProcessQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public AiProcess listOne(AiProcessQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<AiProcess> listPage(AiProcessQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiProcess entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(AiProcess entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(AiProcessDto dto) {
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