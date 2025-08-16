package org.hzai.system.syslog.controller;

import java.util.List;

import org.hzai.system.syslog.entity.SysLog;
import org.hzai.system.syslog.entity.dto.SysLogDto;
import org.hzai.system.syslog.entity.dto.SysLogQueryDto;
import org.hzai.system.syslog.entity.mapper.SysLogMapper;
import org.hzai.system.syslog.service.SysLogService;
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

@Path("/sysLog")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SysLogController {
    @Inject
    SysLogMapper mapper;

    @Inject
    SysLogService sysLogService;


    @GET
    @Path("/getPage")
    public R<PageResult<SysLog>> getPage(@BeanParam SysLogQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(sysLogService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<SysLog>> getByDto(@BeanParam SysLogQueryDto dto) {
        return R.ok(sysLogService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<SysLog>> getAll() {
        return R.ok(sysLogService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(SysLog entity) {
        return R.ok(sysLogService.register(entity));
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<SysLog> update(SysLogDto dto) {
        SysLog entity = SysLog.findById(dto.getId());
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
        SysLog entity = SysLog.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}