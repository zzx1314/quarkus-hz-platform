package org.huazhi.system.sysdict.service;

import java.time.LocalDateTime;
import java.util.List;

import org.huazhi.system.sysdict.entity.SysDict;
import org.huazhi.system.sysdict.entity.dto.SysDictQueryDto;
import org.huazhi.system.sysdict.repository.SysDictRepository;
import org.huazhi.system.sysdictitem.entity.SysDictItem;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SysDictServiceImp implements SysDictService {
    @Inject
    SysDictRepository sysDictRepository;

    @Override
    public List<SysDict> listEntitys() {
        return sysDictRepository.list("isDeleted = ?1", Sort.by("createTime"), 0);
    }

    @Override
    public List<SysDict> listEntitysByDto(SysDictQueryDto sysOrgDto) {
        return sysDictRepository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<SysDict> listPage(SysDictQueryDto dto, PageRequest pageRequest) {
        return sysDictRepository.selectPage(dto, pageRequest);
    }

    @Override
    public Long register(SysDict entity) {
        entity.setIsDeleted(0);
        entity.setCreateTime(LocalDateTime.now());
        sysDictRepository.persist(entity);
        return entity.getId();
    }

    @Override
    public List<SysDictItem> getItemByType(String type) {
        SysDict oneDict = sysDictRepository.find("type = ?1", type).singleResult();
        return oneDict.getSysDictItems();
    }

}