package org.huazhi.system.sysdictitem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.huazhi.system.sysdictitem.entity.SysDictItem;
import org.huazhi.system.sysdictitem.entity.dto.SysDictItemQueryDto;
import org.huazhi.system.sysdictitem.repository.SysDictItemRepository;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SysDictItemServiceImp implements SysDictItemService {
    @Inject
    SysDictItemRepository repository;

    @Override
    public List<SysDictItem> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"), 0);
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
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public SysDictItem getOneByType(String type) {
        return repository.find("type = ?1 and isDeleted = ?2", type, 0).firstResult();
    }
}