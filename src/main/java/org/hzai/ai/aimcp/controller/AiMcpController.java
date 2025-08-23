package org.hzai.ai.aimcp.controller;

import java.util.List;

import org.hzai.ai.aimcp.entity.AiMcp;
import org.hzai.ai.aimcp.entity.dto.AiMcpDto;
import org.hzai.ai.aimcp.entity.dto.AiMcpQueryDto;
import org.hzai.ai.aimcp.service.AiMcpService;
import org.hzai.util.JsonUtil;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;
import org.hzai.util.R;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

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

    @POST
    @Path(value = "/uploadFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	public R<Object> uploadFile(@RestForm("file") FileUpload file, @RestForm String aiMcp) throws Exception {
		AiMcp aiMcpObj = JsonUtil.fromJson(aiMcp, AiMcp.class);
		return aiMcpService.uploadFile(file, aiMcpObj);
	}

    @PUT
    @Path("enableMcp/{id}/{status}")
	public R<Object> enableMcp(@PathParam("id") Long id, @PathParam("status") String status) {
		AiMcp aiMcp = new AiMcp();
		aiMcp.setEnable(status);
		aiMcp.setId(id);
		aiMcpService.register(aiMcp);
		return R.ok();
	}

    @GET
    @Path("allSelectOption")
	public R<Object> findAllBySelectOption() {
		return R.ok(aiMcpService.findAllBySelectOption());
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