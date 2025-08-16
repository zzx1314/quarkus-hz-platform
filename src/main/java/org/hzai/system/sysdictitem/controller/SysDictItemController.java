package org.hzai.system.sysdictitem.controller;

import java.util.List;

import org.hzai.system.sysdictitem.entity.SysDictItem;
import org.hzai.system.sysdictitem.entity.dto.SysDictItemDto;
import org.hzai.system.sysdictitem.entity.dto.SysDictItemQueryDto;
import org.hzai.system.sysdictitem.entity.mapper.SysDictItemMapper;
import org.hzai.system.sysdictitem.service.SysDictItemService;
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

@Path("/sysDictItem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SysDictItemController {
    @Inject
    SysDictItemMapper mapper;

    @Inject
    SysDictItemService sysDictItemService;


    @GET
    @Path("/getPage")
    public R<PageResult<SysDictItem>> getPage(@BeanParam SysDictItemQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(sysDictItemService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<SysDictItem>> getByDto(@BeanParam SysDictItemQueryDto dto) {
        return R.ok(sysDictItemService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<SysDictItem>> getAll() {
        return R.ok(sysDictItemService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(SysDictItem entity) {
        return R.ok(sysDictItemService.register(entity));
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<SysDictItem> update(SysDictItemDto dto) {
        SysDictItem entity = SysDictItem.findById(dto.getId());
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
        SysDictItem entity = SysDictItem.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}