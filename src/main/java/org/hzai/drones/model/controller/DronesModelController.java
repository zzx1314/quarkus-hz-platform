package org.hzai.drones.model.controller;

import java.util.List;

import org.hzai.ai.common.SelectOption;
import org.hzai.drones.model.entity.DronesModel;
import org.hzai.drones.model.entity.dto.DronesModelDto;
import org.hzai.drones.model.entity.dto.DronesModelQueryDto;
import org.hzai.drones.model.service.DronesModelService;
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

@Path("/dronesModel")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesModelController {
    @Inject
    DronesModelService dronesModelService;


    @GET
    @Path("/getPage")
    public R<PageResult<DronesModel>> getPage(@BeanParam DronesModelQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(dronesModelService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<DronesModel>> getByDto(@BeanParam DronesModelQueryDto dto) {
        return R.ok(dronesModelService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<DronesModel>> getAll() {
        return R.ok(dronesModelService.listEntitys());
    }

    @GET
    @Path("/getSelectOption")
    public R<List<SelectOption>> getSelectOption() {
        return R.ok(dronesModelService.getSelectOption());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(DronesModel entity) {
        return R.ok(dronesModelService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<DronesModel> update(DronesModelDto dto) {
        dronesModelService.replaceByDto(dto);
        return R.ok();
    }

    @POST
    @Path(value = "/uploadFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	public R<Object> uploadFile(@RestForm("file") FileUpload file, @RestForm String modelInfo) throws Exception {
		DronesModelDto modelDto = JsonUtil.fromJson(modelInfo, DronesModelDto.class);
		return dronesModelService.uploadFile(file, modelDto);
	}

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        DronesModel entity = DronesModel.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}