package org.hzai.user.controller;

import java.util.List;

import org.hzai.user.entity.SysUser;
import org.hzai.user.repository.SysUserRository;

import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/sysUser")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    SysUserRository sysUserRepository;

    @GET
    public List<SysUser> get() {
        return sysUserRepository.listAll(Sort.by("createTime"));
    }
    
}
