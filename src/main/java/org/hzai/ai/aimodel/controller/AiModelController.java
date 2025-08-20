package org.hzai.ai.aimodel.controller;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.ai.aimodel.entity.AiModel;
import org.hzai.ai.aimodel.entity.dto.AiModelDto;
import org.hzai.ai.aimodel.entity.dto.AiModelQueryDto;
import org.hzai.ai.aimodel.service.AiModelService;
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

@Path("/aiModel")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiModelController {
    @Inject
    AiModelService aiModelService;


    @GET
    @Path("/getPage")
    public R<PageResult<AiModel>> getPage(@BeanParam AiModelQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(aiModelService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<AiModel>> getByDto(@BeanParam AiModelQueryDto dto) {
        return R.ok(aiModelService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<AiModel>> getAll() {
        return R.ok(aiModelService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(AiModel entity) {
        return R.ok(aiModelService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<AiModel> update(AiModelDto dto) {
        aiModelService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        AiModel entity = AiModel.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}