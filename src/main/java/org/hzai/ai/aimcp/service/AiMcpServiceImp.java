package org.hzai.ai.aimcp.service;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.ai.aimcp.entity.AiMcp;
import org.hzai.ai.aimcp.entity.dto.AiMcpQueryDto;
import org.hzai.ai.aimcp.entity.dto.AiMcpDto;
import org.hzai.ai.aimcp.repository.AiMcpRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiMcpServiceImp implements AiMcpService {
    @Inject
    AiMcpRepository repository;
    @Override
    public List<AiMcp> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<AiMcp> listEntitysByDto(AiMcpQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public AiMcp listOne(AiMcpQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<AiMcp> listPage(AiMcpQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(AiMcp entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(AiMcp entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(AiMcpDto dto) {
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