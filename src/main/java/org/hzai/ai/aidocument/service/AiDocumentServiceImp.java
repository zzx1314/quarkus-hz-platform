package org.hzai.ai.aidocument.service;

import java.util.List;

import org.hzai.ai.aidocument.entity.AiDocument;
import org.hzai.ai.aidocument.entity.dto.AiDocumentQueryDto;
import org.hzai.ai.aidocument.repository.AiDocumentRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiDocumentServiceImp implements AiDocumentService {
    @Inject
    AiDocumentRepository repository;
    @Override
    public List<AiDocument> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiDocument> listEntitysByDto(AiDocumentQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiDocument> listPage(AiDocumentQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiDocument entity) {
        repository.persist(entity);
        return true;
    }

}