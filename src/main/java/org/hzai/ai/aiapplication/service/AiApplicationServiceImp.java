package org.hzai.ai.aiapplication.service;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.ai.aiapplication.entity.AiApplication;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationQueryDto;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationDto;
import org.hzai.ai.aiapplication.repository.AiApplicationRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiApplicationServiceImp implements AiApplicationService {
    @Inject
    AiApplicationRepository repository;
    @Override
    public List<AiApplication> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiApplication> listEntitysByDto(AiApplicationQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public AiApplication listOne(AiApplicationQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<AiApplication> listPage(AiApplicationQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiApplication entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(AiApplication entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(AiApplicationDto dto) {
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