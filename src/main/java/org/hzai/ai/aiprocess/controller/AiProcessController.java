package org.hzai.ai.aiprocess.controller;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.ai.aiprocess.entity.AiProcess;
import org.hzai.ai.aiprocess.entity.dto.AiProcessDto;
import org.hzai.ai.aiprocess.entity.dto.AiProcessQueryDto;
import org.hzai.ai.aiprocess.service.AiProcessService;
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

@Path("/aiProcess")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiProcessController {
    @Inject
    AiProcessService aiProcessService;


    @GET
    @Path("/getPage")
    public R<PageResult<AiProcess>> getPage(@BeanParam AiProcessQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(aiProcessService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<AiProcess>> getByDto(@BeanParam AiProcessQueryDto dto) {
        return R.ok(aiProcessService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<AiProcess>> getAll() {
        return R.ok(aiProcessService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(AiProcess entity) {
        return R.ok(aiProcessService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<AiProcess> update(AiProcessDto dto) {
        aiProcessService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        AiProcess entity = AiProcess.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}