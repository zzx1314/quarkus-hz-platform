package org.huazhi.system.sysmenu.service;

import java.util.List;

import org.huazhi.system.sysmenu.entity.SysMenu;
import org.huazhi.system.sysmenu.entity.dto.SysMenuDto;
import org.huazhi.system.sysmenu.entity.dto.SysMenuQueryDto;
import org.huazhi.system.sysmenu.entity.vo.MenuVo;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface SysMenuService {
   List<SysMenu> listEntitys();

   List<SysMenu> listEntitysByDto(SysMenuQueryDto dto);

   PageResult<SysMenu> listPage(SysMenuQueryDto dto, PageRequest pageRequest);

   List<MenuVo> listMenuByRoleId(Long roleId);

   Boolean register(SysMenu entity);

   SysMenu updateMenu(SysMenuDto dto);
}