package org.huazhi.drones.device.controller;

import java.util.List;

import org.huazhi.drones.common.SelectOption;
import org.huazhi.drones.device.entity.DronesDevice;
import org.huazhi.drones.device.entity.dto.DronesDeviceDto;
import org.huazhi.drones.device.entity.dto.DronesDeviceQueryDto;
import org.huazhi.drones.device.service.DronesDeviceService;
import org.huazhi.oauth.annotation.TokenRequired;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;

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
@TokenRequired
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesDeviceController {
    @Inject
    DronesDeviceService dronesDeviceService;

    @GET
    @Path("/getPage")
    public R<PageResult<DronesDevice>> getPage(@BeanParam DronesDeviceQueryDto dto,
            @BeanParam PageRequest pageRequest) {
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
    public R<Long> create(DronesDevice entity) {
        return R.ok(dronesDeviceService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<Void> update(DronesDeviceDto dto) {
        dronesDeviceService.replaceByDto(dto);
        return R.ok();
    }

    @GET
    @Path("/getStatus/{deviceId}")
    public R<DronesDevice> getStatus(@PathParam("deviceId") Long deviceId) {
        DronesDeviceQueryDto queryDto = new DronesDeviceQueryDto();
        queryDto.setId(deviceId);
        DronesDevice entity = dronesDeviceService.listOne(queryDto);
        if (entity == null) {
            return R.failed(null,"设备不存在");
        }
        return R.ok(entity);
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