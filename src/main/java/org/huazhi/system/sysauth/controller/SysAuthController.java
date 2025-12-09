package org.huazhi.system.sysauth.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.huazhi.system.sysauth.dto.SysAuthDto;
import org.huazhi.system.sysauth.service.SysAuthService;
import org.huazhi.system.sysmenu.entity.SysMenu;
import org.huazhi.system.sysmenu.entity.dto.SysMenuQueryDto;
import org.huazhi.system.sysmenu.service.SysMenuService;
import org.huazhi.system.sysrole.entity.SysRole;
import org.huazhi.system.sysrole.entity.dto.SysRoleQueryDto;
import org.huazhi.system.sysrole.service.SysRoleService;
import org.huazhi.util.R;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/sysAuth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SysAuthController {
	@Inject
	SysAuthService sysAuthService;

	@Inject
	SysMenuService sysMenuService;

	@Inject
	SysRoleService sysRoleService;

	@GET
	@Path("/getMenuData/{adminCode}")
	public R<Object> getMenuBtnPath(@PathParam("adminCode") String adminCode) {
		return sysAuthService.getRoleAuth(adminCode);
	}

	@POST
	@Path("/setMenuAuth")
	@Transactional
	public R<Void> setMenuAuth(SysAuthDto sysAuthDto) {
		SysRoleQueryDto sysRoleDto = new SysRoleQueryDto();
		sysRoleDto.setCode(sysAuthDto.getRoleCode());
		SysRole sysRole = sysRoleService.listRoleByDto(sysRoleDto);
		// 将角色和菜单信息保存
		List<Long> authList = sysAuthDto.getAuthList();

		List<SysMenu> allMenu = sysMenuService.listEntitys();
		Map<Long, SysMenu> menuMap = allMenu.stream().collect(Collectors.toMap(SysMenu::getId, sysMenu -> sysMenu));

		Set<Long> menuIdSet = new HashSet<>();
		if (!authList.isEmpty()) {
			SysMenuQueryDto sysMenuQueryDto = new SysMenuQueryDto();
			sysMenuQueryDto.setIds(authList);
			List<SysMenu> sysMenus = sysMenuService.listEntitysByDto(sysMenuQueryDto);
			Set<Long> oneParentIdSet = sysMenus.stream().map(SysMenu::getParentId).collect(Collectors.toSet());
			Set<Long> authSetId = sysMenus.stream().map(SysMenu::getId).collect(Collectors.toSet());

			for (Long oneParentId : oneParentIdSet) {
				menuIdSet.add(oneParentId);
				this.getMenuIds(oneParentId, menuMap, menuIdSet);
			}
			menuIdSet.addAll(authSetId);

			sysMenuQueryDto.setIds(menuIdSet.stream().collect(Collectors.toList()));
			List<SysMenu> newSysMenus = sysMenuService.listEntitysByDto(sysMenuQueryDto);
			sysRole.setMenus(newSysMenus);
		}
		return R.ok();
	}

	private void getMenuIds(Long parntId, Map<Long, SysMenu> menuMap, Set<Long> parentIdSet) {
		SysMenu sysMenu = menuMap.get(parntId);
		if (sysMenu == null) {
			return;
		} else {
			parentIdSet.add(sysMenu.getId());
			getMenuIds(sysMenu.getParentId(), menuMap, parentIdSet);
		}
	}
}
