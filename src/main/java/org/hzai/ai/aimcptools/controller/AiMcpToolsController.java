package org.hzai.ai.aimcptools.controller;

import java.util.List;
import java.time.LocalDateTime;

import org.hzai.ai.aimcptools.entity.AiMcpTools;
import org.hzai.ai.aimcptools.entity.dto.AiMcpToolsDto;
import org.hzai.ai.aimcptools.entity.dto.AiMcpToolsQueryDto;
import org.hzai.ai.aimcptools.service.AiMcpToolsService;
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

@Path("/aiMcpTools")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiMcpToolsController {
    @Inject
    AiMcpToolsService aiMcpToolsService;


    @GET
    @Path("/getPage")
    public R<PageResult<AiMcpTools>> getPage(@BeanParam AiMcpToolsQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(aiMcpToolsService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<AiMcpTools>> getByDto(@BeanParam AiMcpToolsQueryDto dto) {
        return R.ok(aiMcpToolsService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<AiMcpTools>> getAll() {
        return R.ok(aiMcpToolsService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(AiMcpTools entity) {
        return R.ok(aiMcpToolsService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<AiMcpTools> update(AiMcpToolsDto dto) {
        aiMcpToolsService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        AiMcpTools entity = AiMcpTools.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}