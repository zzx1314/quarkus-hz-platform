package org.hzai.ai.aiapplication.controller;

import java.util.List;

import org.hzai.ai.aiapplication.entity.AiApplication;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationDto;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationQueryDto;
import org.hzai.ai.aiapplication.entity.mapper.AiApplicationMapper;
import org.hzai.ai.aiapplication.service.AiApplicationService;
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

@Path("/aiApplication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiApplicationController {
    @Inject
    AiApplicationMapper mapper;

    @Inject
    AiApplicationService aiApplicationService;


    @GET
    @Path("/getPage")
    public R<PageResult<AiApplication>> getPage(@BeanParam AiApplicationQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(aiApplicationService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<AiApplication>> getByDto(@BeanParam AiApplicationQueryDto dto) {
        return R.ok(aiApplicationService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<AiApplication>> getAll() {
        return R.ok(aiApplicationService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(AiApplication entity) {
        return R.ok(aiApplicationService.register(entity));
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<AiApplication> update(AiApplicationDto dto) {
        AiApplication entity = AiApplication.findById(dto.getId());
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
        AiApplication entity = AiApplication.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}