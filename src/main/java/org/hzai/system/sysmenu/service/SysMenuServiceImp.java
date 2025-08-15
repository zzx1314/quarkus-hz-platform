package org.hzai.system.sysmenu.service;

import java.util.List;

import org.hzai.system.sysmenu.entity.SysMenu;
import org.hzai.system.sysmenu.entity.dto.SysMenuQueryDto;
import org.hzai.system.sysmenu.repository.SysMenuRepository;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SysMenuServiceImp implements SysMenuService {
    @Inject
    SysMenuRepository repository;
    @Override
    public List<SysMenu> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<SysMenu> listEntitysByDto(SysMenuQueryDto sysOrgDto) {
        return repository.selectList(sysOrgDto);
    }

    @Override
    public PageResult<SysMenu> listPage(SysMenuQueryDto dto, PageRequest pageRequest) {
        return repository.selectPage(dto, pageRequest);
    }

    @Override
    public Boolean register(SysMenu entity) {
        repository.persist(entity);
        return true;
    }

}