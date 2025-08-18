package org.hzai.ai.aiparagraph.service;

import java.util.List;

import org.hzai.ai.aiparagraph.entity.AiParagraph;
import org.hzai.ai.aiparagraph.entity.dto.AiParagraphQueryDto;
import org.hzai.ai.aiparagraph.repository.AiParagraphRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiParagraphServiceImp implements AiParagraphService {
    @Inject
    AiParagraphRepository repository;
    @Override
    public List<AiParagraph> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiParagraph> listEntitysByDto(AiParagraphQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiParagraph> listPage(AiParagraphQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiParagraph entity) {
        repository.persist(entity);
        return true;
    }

}