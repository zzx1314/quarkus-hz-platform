package org.huazhi.system.sysmenu.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.huazhi.system.sysmenu.entity.SysMenu;
import org.huazhi.system.sysmenu.entity.dto.SysMenuDto;
import org.huazhi.system.sysmenu.entity.dto.SysMenuQueryDto;
import org.huazhi.system.sysmenu.entity.mapper.SysMenuMapper;
import org.huazhi.system.sysmenu.entity.vo.MenuVo;
import org.huazhi.system.sysmenu.repository.SysMenuRepository;
import org.huazhi.system.sysrole.entity.SysRole;
import org.huazhi.system.sysrole.entity.dto.SysRoleQueryDto;
import org.huazhi.system.sysrole.repository.SysRoleRepository;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SysMenuServiceImp implements SysMenuService {
    @Inject
    SysMenuRepository repository;

    @Inject
    SysRoleRepository sysRoleRepository;

    @Inject
    SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> listEntitys() {
        return repository.list("isDeleted = ?1", Sort.by("createTime"), 0);
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
        entity.setCreateTime(LocalDateTime.now());
        repository.persist(entity);
        return true;
    }

    @Override
    public List<MenuVo> listMenuByRoleId(Long roleId) {
        SysRoleQueryDto sysRoleDto = new SysRoleQueryDto();
        sysRoleDto.setId(roleId);
        List<SysRole> roleMenuList = sysRoleRepository.selectRoleList(sysRoleDto);
        if (roleMenuList != null && !roleMenuList.isEmpty()) {
            SysRole role = roleMenuList.get(0);
            List<SysMenu> menus = role.getMenus();

            // 权限组合
            Map<Integer, List<SysMenu>> greoupByPIdMap = menus.stream()
                    .filter(sysMenu -> sysMenu.getType() == 2)
                    .collect(Collectors.groupingBy(SysMenu::getParentId));

            return menus.stream().map(one -> {
                MenuVo menuVo = sysMenuMapper.toMenuVo(one);
                List<SysMenu> buttons = greoupByPIdMap.get(one.getId());
                if (buttons != null && !buttons.isEmpty()) {
                    List<String> auths = buttons.stream().map(SysMenu::getPermission).collect(Collectors.toList());
                    menuVo.setAuths(auths);
                }
                return menuVo;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public SysMenu updateMenu(SysMenuDto dto) {
        SysMenu entity = SysMenu.findById(dto.getId());
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setUpdateTime(LocalDateTime.now());
        sysMenuMapper.updateEntityFromDto(dto, entity);
        return entity;
    }

}