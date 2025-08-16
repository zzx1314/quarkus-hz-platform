package org.hzai.system.sysdictitem.service;

import java.util.List;

import org.hzai.system.sysdictitem.entity.SysDictItem;
import org.hzai.system.sysdictitem.entity.dto.SysDictItemQueryDto;
import org.hzai.system.sysdictitem.repository.SysDictItemRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SysDictItemServiceImp implements SysDictItemService {
    @Inject
    SysDictItemRepository repository;
    @Override
    public List<SysDictItem> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<SysDictItem> listEntitysByDto(SysDictItemQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<SysDictItem> listPage(SysDictItemQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(SysDictItem entity) {
        repository.persist(entity);
        return true;
    }

}