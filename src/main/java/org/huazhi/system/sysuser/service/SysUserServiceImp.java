package org.huazhi.system.sysuser.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

@ApplicationScoped
public class SysUserServiceImp implements SysUserService {
    @Inject
    SysUserRepository sysUserRepository;

    @Inject
    SysUserMapper sysUserMapper;

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
    public Boolean registerUser(SysUser sysUser) {
        sysUser.setCreateTime(LocalDateTime.now());
        sysUserRepository.persist(sysUser);
        return true;
    }

    @Override
    public SysUser listOne(SysUserQueryDto queryDto) {
        return sysUserRepository.selectOne(queryDto);
    }
}
