package org.hzai.system.sysmenu.service;

import java.util.List;

import org.hzai.system.sysmenu.entity.SysMenu;
import org.hzai.system.sysmenu.entity.dto.SysMenuDto;
import org.hzai.system.sysmenu.entity.dto.SysMenuQueryDto;
import org.hzai.system.sysmenu.entity.vo.MenuVo;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface SysMenuService {
   List<SysMenu> listEntitys();

   List<SysMenu> listEntitysByDto(SysMenuQueryDto dto);

   PageResult<SysMenu> listPage(SysMenuQueryDto dto, PageRequest pageRequest);

   List<MenuVo> listMenuByRoleId(Long roleId);

   Boolean register(SysMenu entity);

   SysMenu updateMenu(SysMenuDto dto);
}