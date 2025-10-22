package org.hzai.drones.workflow.service;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.drones.workflow.entity.DronesWorkflow;
import org.hzai.drones.workflow.entity.dto.DronesWorkflowQueryDto;
import org.hzai.drones.workflow.entity.dto.DronesWorkflowDto;
import org.hzai.drones.workflow.repository.DronesWorkflowRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DronesWorkflowServiceImp implements DronesWorkflowService {
    @Inject
    DronesWorkflowRepository repository;
    @Override
    public List<DronesWorkflow> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<DronesWorkflow> listEntitysByDto(DronesWorkflowQueryDto dto) {
        return repository.selectList(dto);
    }

    @Override
    public DronesWorkflow listOne(DronesWorkflowQueryDto dto) {
        return repository.selectOne(dto);
    }

    @Override
    public PageResult<DronesWorkflow> listPage(DronesWorkflowQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(DronesWorkflow entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public void replaceById(DronesWorkflow entity) {
        repository.updateById(entity);
    }

    @Override
    public void replaceByDto(DronesWorkflowDto dto) {
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