package org.hzai.system.sysmenu.controller;

import java.util.List;

import org.hzai.system.sysmenu.entity.SysMenu;
import org.hzai.system.sysmenu.entity.dto.SysMenuDto;
import org.hzai.system.sysmenu.entity.dto.SysMenuQueryDto;
import org.hzai.system.sysmenu.entity.mapper.SysMenuMapper;
import org.hzai.system.sysmenu.service.SysMenuService;
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
    SysMenuMapper mapper;

    @Inject
    SysMenuService sysMenuService;


     @GET
    @Path("/getPage")
    public R<PageResult<SysMenu>> getPage(@BeanParam SysMenuQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(sysMenuService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<SysMenu>> getByDto(@BeanParam SysMenuQueryDto dto) {
        return R.ok(sysMenuService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<SysMenu>> getAll() {
        return R.ok(sysMenuService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(SysMenu entity) {
        return R.ok(sysMenuService.register(entity));
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<SysMenu> update(SysMenuDto dto) {
        SysMenu entity = SysMenu.findById(dto.getId());
        if(entity == null) {
            throw new NotFoundException();
        }
        mapper.updateEntityFromDto(dto, entity);
        return R.ok(entity);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        SysMenu entity = SysMenu.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}