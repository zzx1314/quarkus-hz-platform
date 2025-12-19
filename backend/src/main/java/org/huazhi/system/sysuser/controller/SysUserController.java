package org.huazhi.system.sysuser.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.huazhi.oauth.annotation.TokenRequired;
import org.huazhi.system.sysmenu.entity.SysMenu;
import org.huazhi.system.sysrole.entity.SysRole;
import org.huazhi.system.sysuser.entity.SysUser;
import org.huazhi.system.sysuser.entity.dto.SysUserDto;
import org.huazhi.system.sysuser.entity.dto.SysUserQueryDto;
import org.huazhi.system.sysuser.entity.dto.UserInfo;
import org.huazhi.system.sysuser.entity.mapper.SysUserMapper;
import org.huazhi.system.sysuser.service.SysUserService;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;

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
    @TokenRequired
    public R<PageResult<SysUser>> getPage(@BeanParam SysUserQueryDto sysUserDto, @BeanParam PageRequest pageRequest) {
        return R.ok(userService.listUserPage(sysUserDto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    @TokenRequired
    public R<List<SysUser>> getByDto(@BeanParam SysUserQueryDto sysUserDto) {
        return R.ok(userService.listUsersByDto(sysUserDto));
    }

    @GET
    @Path("/getInfo")
    @TokenRequired
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
    @TokenRequired
    public R<List<SysUser>> getAll() {
        return R.ok(userService.listUsers());
    }

    @POST
    @Path("/create")
    @Transactional
    @TokenRequired
    public R<Void> createUser(SysUserDto sysUser) {
        return userService.registerUser(sysUser);
    }

    @PUT
    @Path("/update")
    @Transactional
    @TokenRequired
    public R<Void> update(SysUserDto sysUserDto) {
        return userService.updateUser(sysUserDto);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @TokenRequired
    public R<Void> delete(@PathParam("id") Long id) {
        SysUser entity = SysUser.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.getRoles().clear(); 
        entity.persist();
        return R.ok();
    }

}
