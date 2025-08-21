package org.hzai.ai.aimcp.controller;

import java.util.List;

import org.hzai.ai.aimcp.entity.AiMcp;
import org.hzai.ai.aimcp.entity.dto.AiMcpDto;
import org.hzai.ai.aimcp.entity.dto.AiMcpQueryDto;
import org.hzai.ai.aimcp.service.AiMcpService;
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

@Path("/aiMcp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiMcpController {
    @Inject
    AiMcpService aiMcpService;


    @GET
    @Path("/getPage")
    public R<PageResult<AiMcp>> getPage(@BeanParam AiMcpQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(aiMcpService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<AiMcp>> getByDto(@BeanParam AiMcpQueryDto dto) {
        return R.ok(aiMcpService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<AiMcp>> getAll() {
        return R.ok(aiMcpService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(AiMcp entity) {
        return R.ok(aiMcpService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<AiMcp> update(AiMcpDto dto) {
        aiMcpService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        AiMcp entity = AiMcp.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}