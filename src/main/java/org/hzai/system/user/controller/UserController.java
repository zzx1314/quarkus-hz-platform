package org.hzai.system.user.controller;

import java.util.List;

import org.hzai.system.user.entity.SysUser;
import org.hzai.system.user.entity.dto.SysUserDto;
import org.hzai.system.user.entity.dto.SysUserQueryDto;
import org.hzai.system.user.entity.mapper.SysUserMapper;
import org.hzai.system.user.service.UserService;
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
public class UserController {
    @Inject
    UserService userService;

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
        entity = sysUserMapper.toEntity(sysUserDto);
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
        entity.delete();
        return R.ok();
    }
    
}
