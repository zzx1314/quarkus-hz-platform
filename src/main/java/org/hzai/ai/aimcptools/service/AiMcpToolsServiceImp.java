package org.hzai.ai.aimcptools.service;

import java.util.List;

import org.hzai.ai.aimcptools.entity.AiMcpTools;
import org.hzai.ai.aimcptools.entity.dto.AiMcpToolsQueryDto;
import org.hzai.ai.aimcptools.repository.AiMcpToolsRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiMcpToolsServiceImp implements AiMcpToolsService {
    @Inject
    AiMcpToolsRepository repository;
    @Override
    public List<AiMcpTools> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiMcpTools> listEntitysByDto(AiMcpToolsQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<AiMcpTools> listPage(AiMcpToolsQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiMcpTools entity) {
        repository.persist(entity);
        return true;
    }

}