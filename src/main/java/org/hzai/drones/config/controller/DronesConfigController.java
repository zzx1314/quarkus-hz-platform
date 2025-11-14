package org.hzai.drones.config.controller;

import java.util.List;

import org.hzai.drones.common.SelectOption;
import org.hzai.drones.config.entity.DronesConfig;
import org.hzai.drones.config.entity.dto.DronesConfigDto;
import org.hzai.drones.config.entity.dto.DronesConfigQueryDto;
import org.hzai.drones.config.service.DronesConfigService;
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

@Path("/dronesConfig")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesConfigController {
    @Inject
    DronesConfigService dronesConfigService;


    @GET
    @Path("/getPage")
    public R<PageResult<DronesConfig>> getPage(@BeanParam DronesConfigQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(dronesConfigService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<DronesConfig>> getByDto(@BeanParam DronesConfigQueryDto dto) {
        return R.ok(dronesConfigService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<DronesConfig>> getAll() {
        return R.ok(dronesConfigService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(DronesConfig entity) {
        return R.ok(dronesConfigService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<DronesConfig> update(DronesConfigDto dto) {
        dronesConfigService.replaceByDto(dto);
        return R.ok();
    }

    @GET
    @Path("/getSelectOption")
    public R<List<SelectOption>> getSelectOption() {
        return R.ok(dronesConfigService.getSelectOptions());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        DronesConfig entity = DronesConfig.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}