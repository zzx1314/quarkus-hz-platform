package org.hzai.system.sysmenu.controller;

import org.hzai.system.sysmenu.entity.mapper.SysMenuMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/sysMenu")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SysMenuController {
    @Inject
    SysMenuMapper sysMenuMapper;
}
