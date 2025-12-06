package org.huazhi.drones.routelibrary.controller;

import java.util.List;

import org.huazhi.drones.common.SelectOption;
import org.huazhi.drones.routelibrary.entity.DronesRouteLibrary;
import org.huazhi.drones.routelibrary.entity.dto.DronesRouteLibraryDto;
import org.huazhi.drones.routelibrary.entity.dto.DronesRouteLibraryQueryDto;
import org.huazhi.drones.routelibrary.service.DronesRouteLibraryService;
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

@Path("/dronesRouteLibrary")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesRouteLibraryController {
    @Inject
    DronesRouteLibraryService dronesRouteLibraryService;

    @GET
    @Path("/getPage")
    public R<PageResult<DronesRouteLibrary>> getPage(@BeanParam DronesRouteLibraryQueryDto dto,
            @BeanParam PageRequest pageRequest) {
        return R.ok(dronesRouteLibraryService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<DronesRouteLibrary>> getByDto(@BeanParam DronesRouteLibraryQueryDto dto) {
        return R.ok(dronesRouteLibraryService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<DronesRouteLibrary>> getAll() {
        return R.ok(dronesRouteLibraryService.listEntitys());
    }

    @GET
    @Path("/getSelectOption")
    public R<List<SelectOption>> getSelectOption() {
        return R.ok(dronesRouteLibraryService.getSelectOptions());
    }

    @GET
    @Path("/getSelectOptionPoint/{id}")
    public R<List<SelectOption>> getSelectOptionPoint(@PathParam("id") Long id) {
        return R.ok(dronesRouteLibraryService.getRouteOption(id));
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(DronesRouteLibrary entity) {
        return R.ok(dronesRouteLibraryService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<DronesRouteLibrary> update(DronesRouteLibraryDto dto) {
        dronesRouteLibraryService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        DronesRouteLibrary entity = DronesRouteLibrary.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

    @GET
    @Path("/getRoute/{modelId}")
    public R<Object> getRoute(@PathParam("modelId") Long modelId) {
        return dronesRouteLibraryService.getRoute(modelId);
    }

    @POST
    @Path("/saveRouteData")
    public R<Boolean> saveRouteData(DronesRouteLibraryDto data) {
        dronesRouteLibraryService.saveRouteData(data);
        return R.ok();
    }

    @GET
    @Path("/startOrStopRoute/{id}/{status}")
    public R<Object> startOrRoute(@PathParam("id") Long id, @PathParam("status") String status) {
        return dronesRouteLibraryService.startOrRoute(id, status);
    }

}