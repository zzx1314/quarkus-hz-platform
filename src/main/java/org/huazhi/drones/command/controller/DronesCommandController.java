package org.huazhi.drones.command.controller;

import java.util.List;

import org.huazhi.drones.command.entity.DronesCommand;
import org.huazhi.drones.command.entity.dto.DronesCommandDto;
import org.huazhi.drones.command.entity.dto.DronesCommandQueryDto;
import org.huazhi.drones.command.service.DronesCommandService;
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

@Path("/dronesCommand")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesCommandController {
    @Inject
    DronesCommandService dronesCommandService;

    @GET
    @Path("/getPage")
    public R<PageResult<DronesCommand>> getPage(@BeanParam DronesCommandQueryDto dto,
            @BeanParam PageRequest pageRequest) {
        return R.ok(dronesCommandService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<DronesCommand>> getByDto(@BeanParam DronesCommandQueryDto dto) {
        return R.ok(dronesCommandService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<DronesCommand>> getAll() {
        return R.ok(dronesCommandService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(DronesCommand entity) {
        return R.ok(dronesCommandService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<Void> update(DronesCommandDto dto) {
        dronesCommandService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        DronesCommand entity = DronesCommand.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}