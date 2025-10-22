package org.hzai.drones.media.controller;

import java.util.List;

import org.hzai.drones.media.entity.DronesMedia;
import org.hzai.drones.media.entity.dto.DronesMediaDto;
import org.hzai.drones.media.entity.dto.DronesMediaQueryDto;
import org.hzai.drones.media.service.DronesMediaService;
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

@Path("/dronesMedia")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesMediaController {
    @Inject
    DronesMediaService dronesMediaService;


    @GET
    @Path("/getPage")
    public R<PageResult<DronesMedia>> getPage(@BeanParam DronesMediaQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(dronesMediaService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<DronesMedia>> getByDto(@BeanParam DronesMediaQueryDto dto) {
        return R.ok(dronesMediaService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<DronesMedia>> getAll() {
        return R.ok(dronesMediaService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(DronesMedia entity) {
        return R.ok(dronesMediaService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<DronesMedia> update(DronesMediaDto dto) {
        dronesMediaService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        DronesMedia entity = DronesMedia.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}