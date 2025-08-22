package org.hzai.ai.aiknowledgebase.controller;

import java.util.List;

import org.hzai.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseDto;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseQueryDto;
import org.hzai.ai.aiknowledgebase.entity.vo.AiKnowledgeBaseVo;
import org.hzai.ai.aiknowledgebase.service.AiKnowledgeBaseService;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.R;
import org.hzai.util.SecurityUtil;

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
    AiKnowledgeBaseService aiKnowledgeBaseService;

    @Inject
    SecurityUtil securityUtil;


    @GET
    @Path("/getPage")
    public R<PageResult<AiKnowledgeBaseVo>> getPage(@BeanParam AiKnowledgeBaseQueryDto dto, @BeanParam PageRequest pageRequest) {
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

    @GET
    @Path("/getById/{id}")
	public R<Object> getById(@PathParam("id") Long id) {
        AiKnowledgeBaseQueryDto queryDto = new AiKnowledgeBaseQueryDto();
        queryDto.setId(id);
		AiKnowledgeBase one = aiKnowledgeBaseService.listOne(queryDto);
		return R.ok(one);
	}

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(AiKnowledgeBase entity) {
        entity.setCreateUser(securityUtil.getUserName());
        return R.ok(aiKnowledgeBaseService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<AiKnowledgeBase> update(AiKnowledgeBaseDto dto) {
        aiKnowledgeBaseService.replaceByDto(dto);
        return R.ok();
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