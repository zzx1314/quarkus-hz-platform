package org.huazhi.drones.services.controller;

import java.util.List;

import org.huazhi.drones.services.entity.DronesServices;
import org.huazhi.drones.services.entity.dto.DronesServicesDto;
import org.huazhi.drones.services.entity.dto.DronesServicesQueryDto;
import org.huazhi.drones.services.service.DronesServicesService;
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

@Path("/dronesServices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesServicesController {
    @Inject
    DronesServicesService dronesServicesService;


    @GET
    @Path("/getPage")
    public R<PageResult<DronesServices>> getPage(@BeanParam DronesServicesQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(dronesServicesService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<DronesServices>> getByDto(@BeanParam DronesServicesQueryDto dto) {
        return R.ok(dronesServicesService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<DronesServices>> getAll() {
        return R.ok(dronesServicesService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(DronesServices entity) {
        return R.ok(dronesServicesService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<DronesServices> update(DronesServicesDto dto) {
        dronesServicesService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        DronesServices entity = DronesServices.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}