package org.hzai.user.controller;

import java.util.List;

import org.hzai.user.entity.SysUser;
import org.hzai.user.entity.dto.SysUserDto;
import org.hzai.user.service.UserService;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

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
    public PageResult<SysUser> getPage(@BeanParam SysUserDto sysUserDto, @BeanParam PageRequest pageRequest) {
        return userService.listUserPage(sysUserDto, pageRequest);
    }

    @GET
    @Path("/getByDto")
    public List<SysUser> getByDto(SysUserDto sysUserDto) {
        return userService.listUsersByDto(sysUserDto);
    }

    @GET
    @Path("/getAll")
    public List<SysUser> getAll() {
        return userService.listUsers();
    }
    
}
