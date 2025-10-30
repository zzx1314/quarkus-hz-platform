package org.hzai.drones.device.controller;

import java.util.List;

import org.hzai.ai.common.SelectOption;
import org.hzai.drones.device.entity.DronesDevice;
import org.hzai.drones.device.entity.dto.DronesDeviceDto;
import org.hzai.drones.device.entity.dto.DronesDeviceQueryDto;
import org.hzai.drones.device.service.DronesDeviceService;
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

@Path("/dronesDevice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesDeviceController {
    @Inject
    DronesDeviceService dronesDeviceService;


    @GET
    @Path("/getPage")
    public R<PageResult<DronesDevice>> getPage(@BeanParam DronesDeviceQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(dronesDeviceService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<DronesDevice>> getByDto(@BeanParam DronesDeviceQueryDto dto) {
        return R.ok(dronesDeviceService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<DronesDevice>> getAll() {
        return R.ok(dronesDeviceService.listEntitys());
    }

    
    @GET
    @Path("/getSelectOption")
    public R<List<SelectOption>> getSelectOption() {
        return R.ok(dronesDeviceService.getSelectOptions());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(DronesDevice entity) {
        return R.ok(dronesDeviceService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<DronesDevice> update(DronesDeviceDto dto) {
        dronesDeviceService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        DronesDevice entity = DronesDevice.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}