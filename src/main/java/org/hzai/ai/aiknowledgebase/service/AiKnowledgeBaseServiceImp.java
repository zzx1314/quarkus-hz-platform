package org.hzai.ai.aiknowledgebase.service;

import java.util.List;

import org.hzai.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseQueryDto;
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
    public List<AiKnowledgeBase> listEntitysByDto(AiKnowledgeBaseQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiKnowledgeBase> listPage(AiKnowledgeBaseQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiKnowledgeBase entity) {
        repository.persist(entity);
        return true;
    }

}