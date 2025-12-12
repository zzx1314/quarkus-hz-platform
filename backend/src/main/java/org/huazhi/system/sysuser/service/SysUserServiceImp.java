package org.huazhi.system.sysuser.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.huazhi.system.sysdictitem.entity.SysDictItem;
import org.huazhi.system.sysdictitem.service.SysDictItemService;
import org.huazhi.system.sysmenu.entity.SysMenu;
import org.huazhi.system.sysrole.entity.SysRole;
import org.huazhi.system.sysuser.entity.SysUser;
import org.huazhi.system.sysuser.entity.dto.SysUserDto;
import org.huazhi.system.sysuser.entity.dto.SysUserQueryDto;
import org.huazhi.system.sysuser.entity.mapper.SysUserMapper;
import org.huazhi.system.sysuser.repository.SysUserRepository;
import org.huazhi.util.AESUtils;
import org.huazhi.util.MD5Util;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SysUserServiceImp implements SysUserService {
    @Inject
    SysUserRepository sysUserRepository;

    @Inject
    SysUserMapper sysUserMapper;

    @Inject
    SysDictItemService sysDictItemService;

    @Override
    public List<SysUser> listUsers() {
        return sysUserRepository.list("isDeleted = ?1", Sort.by("createTime"), 0);
    }

    @Override
    public R<SysUserDto> authenticate(String username, String password) {
        // 先解密
        password = AESUtils.decrypt(password, null);
        Optional<SysUser> firstResultOptional = sysUserRepository.find("username = ?1", username).firstResultOptional();
        if (firstResultOptional.isEmpty()) {
            return R.failed(null, "用户名不存在");
        }
        SysUser sysUser = firstResultOptional.get();
        SysUserDto sysUserDto = sysUserMapper.toDto(sysUser);

        List<SysRole> sysRoleList = sysUser.getRoles();
        if (sysRoleList != null && !sysRoleList.isEmpty()) {
            // 补充角色
            List<Long> roleIdList = sysRoleList.stream().map(SysRole::getId).collect(Collectors.toList());
            sysUserDto.setRoleIdList(roleIdList);

            // 补充权限
            List<String> permissionList = sysRoleList.stream()
                    .flatMap(role -> role.getMenus().stream())
                    .filter(menu -> menu.getPermission() != null && menu.getPermission().length() > 0)
                    .map(SysMenu::getPermission)
                    .collect(Collectors.toList());
            sysUserDto.setPermissions(permissionList);
        }
        boolean verifyResult = MD5Util.verify(password, sysUser.getPassword());
        if (!verifyResult) {
            return R.failed(null, "密码错误");
        }
        return R.ok(sysUserDto, "登录成功");
    }

    @Override
    public List<SysUser> listUsersByDto(SysUserQueryDto sysUserDto) {
        return sysUserRepository.selectUserList(sysUserDto);
    }

    @Override
    public PageResult<SysUser> listUserPage(SysUserQueryDto dto, PageRequest pageRequest) {
        return sysUserRepository.selectUserPage(dto, pageRequest);
    }

    @Override
    public R<Void> registerUser(SysUserDto sysUserDto) {
        Boolean checkResult = this.checkUserName(sysUserDto.getUsername());
		if (!checkResult){
			// 没有通过检查
			return R.failed("账号已存在！");
		}
        SysUser sysUser = sysUserMapper.toEntity(sysUserDto);
        sysUser.setIsDeleted(0);
		sysUser.setPassword("{MD5}" + MD5Util.encrypt(sysUserDto.getPassword()));
        sysUser.setCreateTime(LocalDateTime.now());

        SysDictItem sysPassChange = sysDictItemService.getOneByType("sysPassChange");
		int passChane = Integer.parseInt(sysPassChange.getValue());
		LocalDateTime passUpdateTime = LocalDateTime.now();
		sysUser.setPassUpdateTime(passUpdateTime.minusDays(passChane));
        // 用户绑定多个角色
		if (sysUserDto.getRoleIdList() != null){
			for (Long roleId : sysUserDto.getRoleIdList()) {
				SysRole sysRole = new SysRole();
				sysRole.setId(roleId);
				sysUser.getRoles().add(sysRole);
			}
		}
        // 用户绑定单个角色
		if (sysUserDto.getRole() != null){
			SysRole sysRole = new SysRole();
            sysRole.setId(sysUserDto.getRole());
            sysUser.getRoles().add(sysRole);
		}
        sysUserRepository.persist(sysUser);
        return R.ok();
    }

    public Boolean checkUserName(String userName){
		// 查询是否有重名的账号
		List<SysUser> list = sysUserRepository.list("username = ?1 and isDeleted = ?2", userName, 0);
		if (list.isEmpty() == false) {
			return false;
		}
		return true;
	}

    @Override
    public SysUser listOne(SysUserQueryDto queryDto) {
        return sysUserRepository.selectOne(queryDto);
    }

    @Override
    public R<Void> updateUser(SysUserDto sysUserDto) {
        SysUser entity = SysUser.findById(sysUserDto.getId());
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setUpdateTime(LocalDateTime.now());
        sysUserMapper.updateEntityFromDto(sysUserDto, entity);

         if (sysUserDto.getPassword().isBlank() == false) {
			entity.setPassword("{MD5}" + MD5Util.encrypt(sysUserDto.getPassword()));
		}
        entity.getRoles().clear();
        Set<Long> roleIds = new HashSet<>();

        if (sysUserDto.getRoleIdList() != null) {
            roleIds.addAll(sysUserDto.getRoleIdList());
        }

        if (sysUserDto.getRole() != null) {
            roleIds.add(sysUserDto.getRole());
        }

        for (Long roleId : roleIds) {
            SysRole role = SysRole.findById(roleId);
            if (role != null) {
                entity.getRoles().add(role);
            }
        }
        return R.ok();
    }
}
