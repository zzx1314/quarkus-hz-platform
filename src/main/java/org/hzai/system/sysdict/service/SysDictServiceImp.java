package org.hzai.system.sysdict.service;

import java.time.LocalDateTime;
import java.util.List;

import org.hzai.system.sysdict.entity.SysDict;
import org.hzai.system.sysdict.entity.dto.SysDictQueryDto;
import org.hzai.system.sysdict.repository.SysDictRepository;
import org.hzai.system.sysdictitem.entity.SysDictItem;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SysDictServiceImp implements SysDictService {
    @Inject
    SysDictRepository repository;
    @Override
    public List<SysDict> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<SysDict> listEntitysByDto(SysDictQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<SysDict> listPage(SysDictQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(SysDict entity) {
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public List<SysDictItem> getItemByType(String type) {
         SysDict oneDict = repository.find("type = ?1", type).singleResult();
         return oneDict.getSysDictItems();
    }

}