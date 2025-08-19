package org.hzai.system.sysuser.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.hzai.system.sysmenu.entity.SysMenu;
import org.hzai.system.sysrole.entity.SysRole;
import org.hzai.system.sysuser.entity.SysUser;
import org.hzai.system.sysuser.entity.dto.SysUserDto;
import org.hzai.system.sysuser.entity.dto.SysUserQueryDto;
import org.hzai.system.sysuser.entity.dto.UserInfo;
import org.hzai.system.sysuser.entity.mapper.SysUserMapper;
import org.hzai.system.sysuser.service.SysUserService;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.R;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/sysUser")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SysUserController {
    @Inject
    SysUserService userService;

    @Inject
    SysUserMapper sysUserMapper;

    @GET
    @Path("/getPage")
    public R<PageResult<SysUser>> getPage(@BeanParam SysUserQueryDto sysUserDto, @BeanParam PageRequest pageRequest) {
        return R.ok(userService.listUserPage(sysUserDto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<SysUser>> getByDto(@BeanParam SysUserQueryDto sysUserDto) {
        return R.ok(userService.listUsersByDto(sysUserDto));
    }

    @GET
    @Path("/getInfo")
    public R<UserInfo> getInfo(@BeanParam SysUserQueryDto sysUserDto) {
        UserInfo userInfo = new UserInfo();
        SysUser users = userService.listOne(sysUserDto);
        if (users == null) {
            return R.failed(null, "用户不存在");
        }
        List<SysRole> sysRoleList = users.getRoles();
        // 补充角色
        List<Long> roleIdList = sysRoleList.stream().map(SysRole::getId).collect(Collectors.toList());
        userInfo.setRoles(roleIdList);

        // 补充权限
        List<String> permissionList = sysRoleList.stream()
                .flatMap(role -> role.getMenus().stream())
                .filter(menu -> menu.getPermission() != null && menu.getPermission().length() > 0)
                .map(SysMenu::getPermission)
                .collect(Collectors.toList());
        userInfo.setPermissions(permissionList);
        userInfo.setSysUser(users);
        return R.ok(userInfo);
    }


    @GET
    @Path("/getAll")
    public R<List<SysUser>> getAll() {
        return R.ok(userService.listUsers());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> createUser(SysUser sysUser) {
        return R.ok(userService.registerUser(sysUser));
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<SysUser> update(SysUserDto sysUserDto) {
        SysUser entity = SysUser.findById(sysUserDto.getId());
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.setUpdateTime(LocalDateTime.now());
        sysUserMapper.updateEntityFromDto(sysUserDto, entity);
        return R.ok(entity);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        SysUser entity = SysUser.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }
    
}
