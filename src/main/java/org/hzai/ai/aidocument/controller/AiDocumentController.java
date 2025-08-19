package org.hzai.ai.aidocument.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.hzai.ai.aidocument.entity.AiDocument;
import org.hzai.ai.aidocument.entity.dto.AiDocumentDto;
import org.hzai.ai.aidocument.entity.dto.AiDocumentQueryDto;
import org.hzai.ai.aidocument.entity.mapper.AiDocumentMapper;
import org.hzai.ai.aidocument.service.AiDocumentService;
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

@Path("/aiDocument")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiDocumentController {
    @Inject
    AiDocumentMapper mapper;

    @Inject
    AiDocumentService aiDocumentService;


    @GET
    @Path("/getPage")
    public R<PageResult<AiDocument>> getPage(@BeanParam AiDocumentQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(aiDocumentService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<AiDocument>> getByDto(@BeanParam AiDocumentQueryDto dto) {
        return R.ok(aiDocumentService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<AiDocument>> getAll() {
        return R.ok(aiDocumentService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(AiDocument entity) {
        return R.ok(aiDocumentService.register(entity));
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<AiDocument> update(AiDocumentDto dto) {
        AiDocument entity = AiDocument.findById(dto.getId());
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.setUpdateTime(LocalDateTime.now());
        mapper.updateEntityFromDto(dto, entity);
        return R.ok(entity);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        AiDocument entity = AiDocument.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}