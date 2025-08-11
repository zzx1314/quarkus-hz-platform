package org.hzai.system.user.controller;

import java.util.List;

import org.hzai.system.user.entity.SysUser;
import org.hzai.system.user.entity.dto.SysUserDto;
import org.hzai.system.user.service.UserService;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.R;

import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/sysUser")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    UserService userService;

    @GET
    @Path("/getPage")
    public R<PageResult<SysUser>> getPage(@BeanParam SysUserDto sysUserDto, @BeanParam PageRequest pageRequest) {
        return R.ok(userService.listUserPage(sysUserDto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<SysUser>> getByDto(SysUserDto sysUserDto) {
        return R.ok(userService.listUsersByDto(sysUserDto));
    }

    @GET
    @Path("/getAll")
    public R<List<SysUser>> getAll() {
        return R.ok(userService.listUsers());
    }

    public R<Boolean> createUser(SysUserDto sysUserDto) {
        return R.ok(userService.registerUser(sysUserDto));
    }
    
}
