package org.hzai.ai.aiknowledgebase.service;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseQueryDto;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseDto;
import org.hzai.ai.aiknowledgebase.repository.AiKnowledgeBaseRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiKnowledgeBaseServiceImp implements AiKnowledgeBaseService {
    @Inject
    AiKnowledgeBaseRepository repository;
    @Override
    public List<AiKnowledgeBase> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiKnowledgeBase> listEntitysByDto(AiKnowledgeBaseQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public AiKnowledgeBase listOne(AiKnowledgeBaseQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<AiKnowledgeBase> listPage(AiKnowledgeBaseQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiKnowledgeBase entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(AiKnowledgeBase entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(AiKnowledgeBaseDto dto) {
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