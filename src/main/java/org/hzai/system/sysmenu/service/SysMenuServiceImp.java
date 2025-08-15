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
public class SysMenuServiceImp implements SysMenuService{
    @Inject
    SysMenuRepository sysMenuRepository;

    @Override
    public List<SysMenu> listMenus() {
        return sysMenuRepository.list("isDeleted = ?1", Sort.by("createTime"),  0);
    }

    @Override
    public List<SysMenu> listMenusByDto(SysMenuQueryDto sysMenuDto) {
        return sysMenuRepository.selectMenuList(sysMenuDto);
    }

    @Override
    public PageResult<SysMenu> listMenuPage(SysMenuQueryDto dto, PageRequest pageRequest) {
        return sysMenuRepository.selectMenuPage(dto, pageRequest);
    }

    @Override
    public Boolean registerMenu(SysMenu sysMenu) {
        sysMenuRepository.persist(sysMenu);
        return true;
    }

}
