package org.hzai.ai.aifinetuning.controller;

import java.util.List;

import org.hzai.ai.aifinetuning.entity.AiFineTuning;
import org.hzai.ai.aifinetuning.entity.dto.AiFineTuningDto;
import org.hzai.ai.aifinetuning.entity.dto.AiFineTuningQueryDto;
import org.hzai.ai.aifinetuning.service.AiFineTuningService;
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

@Path("/aiFineTuning")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiFineTuningController {
    @Inject
    AiFineTuningService aiFineTuningService;


    @GET
    @Path("/getPage")
    public R<PageResult<AiFineTuning>> getPage(@BeanParam AiFineTuningQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(aiFineTuningService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<AiFineTuning>> getByDto(@BeanParam AiFineTuningQueryDto dto) {
        return R.ok(aiFineTuningService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<AiFineTuning>> getAll() {
        return R.ok(aiFineTuningService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(AiFineTuning entity) {
        return R.ok(aiFineTuningService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<AiFineTuning> update(AiFineTuningDto dto) {
        aiFineTuningService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        AiFineTuning entity = AiFineTuning.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}