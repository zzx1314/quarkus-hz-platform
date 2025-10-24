package org.hzai.drones.command.controller;

import java.util.List;

import org.hzai.drones.command.entity.DronesCommand;
import org.hzai.drones.command.entity.dto.DronesCommandDto;
import org.hzai.drones.command.entity.dto.DronesCommandQueryDto;
import org.hzai.drones.command.service.DronesCommandService;
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

@Path("/dronesCommand")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesCommandController {
    @Inject
    DronesCommandService dronesCommandService;


    @GET
    @Path("/getPage")
    public R<PageResult<DronesCommand>> getPage(@BeanParam DronesCommandQueryDto dto, @BeanParam PageRequest pageRequest) {
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
    public R<DronesCommand> update(DronesCommandDto dto) {
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