package org.huazhi.ai.aiapplication.controller;

import java.util.List;

import jakarta.ws.rs.*;

import org.huazhi.ai.aiapplication.entity.AiApplication;
import org.huazhi.ai.aiapplication.entity.dto.AiApplicationDto;
import org.huazhi.ai.aiapplication.entity.dto.AiApplicationQueryDto;
import org.huazhi.ai.aiapplication.entity.vo.AiApplicationVo;
import org.huazhi.ai.aiapplication.service.AiApplicationService;
import org.huazhi.util.FileUtil;
import org.huazhi.util.IdUtil;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/aiApplication")
@Blocking
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AiApplicationController {
    @Inject
    AiApplicationService aiApplicationService;


    @GET
    @Path("/getPage")
    public R<PageResult<AiApplicationVo>> getPage(@BeanParam AiApplicationQueryDto dto, @BeanParam PageRequest pageRequest) {
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
    public R<Boolean> create(AiApplicationDto entity) {
        return R.ok(aiApplicationService.register(entity));
    }

    @PUT
    @Path("update")
	public R<Object> update(AiApplicationDto aiApplication) {
		aiApplicationService.replaceData(aiApplication);
		return R.ok("success");
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

    @GET
    @Path("/isEnable/{id}/{isEnable}")
	public R<Object> findById(@PathParam("id") Long id, @PathParam("isEnable") Boolean isEnable) {
		AiApplication aiApplication = new AiApplication().setId(id).setIsSetup(isEnable);
		aiApplicationService.replaceById(aiApplication);
		return R.ok("success");
	}

    @GET
    @Produces("text/stream;charset=UTF-8")
    @Path(value = "/chat")
	public Multi<String> chatStream(@QueryParam("appId") Long appId, @QueryParam("message")  String message) {
		return aiApplicationService.chat(appId, message, null);
	}

    @POST
    @Produces("text/stream;charset=UTF-8")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path(value = "/chatFile")
	public Multi<String> uploadFile(@RestForm("file") FileUpload file, @RestForm Long appId,
			@RestForm(value = "message") String message) {
        String fileTemp = IdUtil.simpleUUID();
		String filepath = FileUtil.saveFile(file, "/temp/file/" + fileTemp).toString();
		return aiApplicationService.chat(appId, message, filepath);
	}

    @GET
    @Path("/downFile")
	public Response downloadByPath(@QueryParam("filePath")  String filePath) {
		 return FileUtil.downloadFile(filePath);
	 }

}