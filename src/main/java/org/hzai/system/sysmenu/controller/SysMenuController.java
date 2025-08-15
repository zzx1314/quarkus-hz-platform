package org.hzai.system.sysmenu.controller;

import java.util.List;

import org.hzai.system.sysmenu.entity.SysMenu;
import org.hzai.system.sysmenu.entity.dto.SysMenuDto;
import org.hzai.system.sysmenu.entity.dto.SysMenuQueryDto;
import org.hzai.system.sysmenu.entity.mapper.SysMenuMapper;
import org.hzai.system.sysmenu.service.SysMenuService;
import org.hzai.system.sysuser.entity.SysUser;
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

@Path("/sysMenu")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SysMenuController {
    @Inject
    SysMenuMapper sysMenuMapper;

    @Inject
    SysMenuService sysMenuService;

    @GET
    @Path("/getPage")
    public R<PageResult<SysMenu>> getPage(@BeanParam SysMenuQueryDto sysMenuDto, @BeanParam PageRequest pageRequest) {
        return R.ok(sysMenuService.listMenuPage(sysMenuDto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<SysMenu>> getByDto(@BeanParam SysMenuQueryDto sysMenuDto) {
        return R.ok(sysMenuService.listMenusByDto(sysMenuDto));
    }

    @GET
    @Path("/getAll")
    public R<List<SysMenu>> getAll() {
        return R.ok(sysMenuService.listMenus());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> createUser(SysMenu sysMenu) {
        return R.ok(sysMenuService.registerMenu(sysMenu));
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<SysMenu> update(SysMenuDto sysMenuDto) {
        SysMenu entity = SysMenu.findById(sysMenuDto.getId());
        if(entity == null) {
            throw new NotFoundException();
        }
        sysMenuMapper.updateEntityFromDto(sysMenuDto, entity);
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
