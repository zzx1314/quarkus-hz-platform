package org.hzai.system.menu.service;

import java.util.List;

import org.hzai.system.menu.entity.SysMenu;
import org.hzai.system.menu.entity.dto.SysMenuQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface SysMenuService {
    List<SysMenu> listMenus();

   List<SysMenu> listMenusByDto(SysMenuQueryDto sysMenuDto);

   PageResult<SysMenu> listMenuPage(SysMenuQueryDto dto, PageRequest pageRequest);

   Boolean registerMenu(SysMenu sysMenu);
}
