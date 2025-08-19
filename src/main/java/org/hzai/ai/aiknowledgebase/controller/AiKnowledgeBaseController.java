package org.hzai.ai.aiknowledgebase.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.hzai.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseDto;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseQueryDto;
import org.hzai.ai.aiknowledgebase.entity.mapper.AiKnowledgeBaseMapper;
import org.hzai.ai.aiknowledgebase.service.AiKnowledgeBaseService;
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

@Path("/aiKnowledgeBase")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiKnowledgeBaseController {
    @Inject
    AiKnowledgeBaseMapper mapper;

    @Inject
    AiKnowledgeBaseService aiKnowledgeBaseService;


    @GET
    @Path("/getPage")
    public R<PageResult<AiKnowledgeBase>> getPage(@BeanParam AiKnowledgeBaseQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(aiKnowledgeBaseService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<AiKnowledgeBase>> getByDto(@BeanParam AiKnowledgeBaseQueryDto dto) {
        return R.ok(aiKnowledgeBaseService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<AiKnowledgeBase>> getAll() {
        return R.ok(aiKnowledgeBaseService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(AiKnowledgeBase entity) {
        return R.ok(aiKnowledgeBaseService.register(entity));
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<AiKnowledgeBase> update(AiKnowledgeBaseDto dto) {
        AiKnowledgeBase entity = AiKnowledgeBase.findById(dto.getId());
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
        AiKnowledgeBase entity = AiKnowledgeBase.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}