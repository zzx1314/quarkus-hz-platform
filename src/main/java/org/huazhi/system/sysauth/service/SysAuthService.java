package org.huazhi.system.sysauth.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.huazhi.system.sysauth.mapper.SysAuthMapper;
import org.huazhi.system.sysauth.vo.SysAuthMenuVo;
import org.huazhi.system.sysauth.vo.SysAuthTitleVo;
import org.huazhi.system.sysmenu.entity.SysMenu;
import org.huazhi.system.sysmenu.entity.dto.SysMenuQueryDto;
import org.huazhi.system.sysmenu.service.SysMenuService;
import org.huazhi.system.sysrole.entity.SysRole;
import org.huazhi.system.sysrole.entity.dto.SysRoleQueryDto;
import org.huazhi.system.sysrole.service.SysRoleService;
import org.huazhi.util.EntityUtils;
import org.huazhi.util.R;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SysAuthService {
	@Inject
	private SysMenuService sysMenuService;

	@Inject
	private SysRoleService sysRoleService;

	@Inject
	SysAuthMapper sysAuthMapper;

	public R<Object> getRoleAuth(String adminCode) {
		List<SysAuthMenuVo> sysMenuVoList = new ArrayList<>();
		// 所拥有的菜单
		SysMenuQueryDto sysMenuQueryDto = new SysMenuQueryDto();
		sysMenuQueryDto.setRoleCode(adminCode);
		List<SysMenu> allMenu = sysMenuService.listEntitysByDto(sysMenuQueryDto);
		if (!allMenu.isEmpty()) {
			Map<Integer, SysMenu> allMenuMap = EntityUtils.toMap(allMenu, SysMenu::getId, e -> e);
			// 找到按钮
			sysMenuQueryDto.setType(2);
			List<SysMenu> sysMenuList = sysMenuService.listEntitysByDto(sysMenuQueryDto);
			// 将按钮按照父节点进行分组
			Map<Integer, List<SysMenu>> parentIdMap = sysMenuList.stream()
					.collect(Collectors.groupingBy(SysMenu::getParentId));
			try {
				parentIdMap.forEach((key, value) -> {
					List<String> menuPathList = new ArrayList<>();
					this.getMenPath(key, menuPathList, allMenuMap);
					SysAuthMenuVo sysAuthMenuVo = new SysAuthMenuVo();
					Collections.reverse(menuPathList);
					sysAuthMenuVo.setTitle(menuPathList.stream().collect(Collectors.joining("/")));

					sysAuthMenuVo.setAuthList(value.stream().map(one -> {
						SysAuthTitleVo sysAuthTitleVo = sysAuthMapper.toSysAuthTitleVo(one);
						return sysAuthTitleVo;
					}).collect(Collectors.toList()));
					// 获取已使用的菜单
					sysAuthMenuVo.setUseAuthList(this.getUseMenu(adminCode, key));

					sysAuthMenuVo
							.setIsCheckAll(sysAuthMenuVo.getAuthList().size() == sysAuthMenuVo.getUseAuthList().size());
					sysMenuVoList.add(sysAuthMenuVo);
				});
			} catch (Exception e) {
				return R.failed(null,"当前角色的权限路径不完整，请再菜单管理检查角色权限范围路径！");
			}
			for (int i = 0; i < sysMenuVoList.size(); i++) {
				SysAuthMenuVo sysAuthMenuVo = sysMenuVoList.get(i);
				sysAuthMenuVo.setId(i);
			}
		}
		List<SysAuthMenuVo> result = sysMenuVoList.stream()
				.sorted(Comparator.comparing(SysAuthMenuVo::getTitle))
				.collect(Collectors.toList());
		return R.ok(result);
	}

	private void getMenPath(Integer id, List<String> menuPathList, Map<Integer, SysMenu> allMenuMap) {
		if (id != -1) {
			SysMenu sysMenu = allMenuMap.get(id);
			menuPathList.add(sysMenu.getName());
			this.getMenPath(sysMenu.getParentId(), menuPathList, allMenuMap);
		}
	}

	private Set<Integer> getUseMenu(String adminCode, Integer id) {
		SysMenuQueryDto queryDto = new SysMenuQueryDto();
		queryDto.setParentId(id);
		List<SysMenu> menuList = sysMenuService.listEntitysByDto(queryDto);
		List<Integer> mensonId = menuList.stream().map(SysMenu::getId).collect(Collectors.toList());

		SysRoleQueryDto roleQueryDto = new SysRoleQueryDto();
		roleQueryDto.setCode(adminCode);
		SysRole sysRole = sysRoleService.listRoleByDto(roleQueryDto);
		List<Integer> allMenuId = sysRole.getMenus().stream().map(SysMenu::getId).collect(Collectors.toList());

		// 取交集
		Set<Integer> intersection = mensonId.stream().filter(allMenuId::contains).collect(Collectors.toSet());
		return intersection;
	}
}
