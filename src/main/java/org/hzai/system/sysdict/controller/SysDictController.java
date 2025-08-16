package org.hzai.system.sysdict.controller;

import java.util.List;

import org.hzai.system.sysdict.entity.SysDict;
import org.hzai.system.sysdict.entity.dto.SysDictDto;
import org.hzai.system.sysdict.entity.dto.SysDictQueryDto;
import org.hzai.system.sysdict.entity.mapper.SysDictMapper;
import org.hzai.system.sysdict.service.SysDictService;
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

@Path("/sysDict")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SysDictController {
    @Inject
    SysDictMapper mapper;

    @Inject
    SysDictService sysDictService;


    @GET
    @Path("/getPage")
    public R<PageResult<SysDict>> getPage(@BeanParam SysDictQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(sysDictService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<SysDict>> getByDto(@BeanParam SysDictQueryDto dto) {
        return R.ok(sysDictService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<SysDict>> getAll() {
        return R.ok(sysDictService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(SysDict entity) {
        return R.ok(sysDictService.register(entity));
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<SysDict> update(SysDictDto dto) {
        SysDict entity = SysDict.findById(dto.getId());
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
        SysDict entity = SysDict.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}