package org.huazhi.drones.routeitem.controller;

import java.util.List;

import org.huazhi.drones.routeitem.entity.DronesRouteItem;
import org.huazhi.drones.routeitem.entity.dto.DronesRouteItemDto;
import org.huazhi.drones.routeitem.entity.dto.DronesRouteItemQueryDto;
import org.huazhi.drones.routeitem.service.DronesRouteItemService;
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

@Path("/dronesRouteItem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesRouteItemController {
    @Inject
    DronesRouteItemService dronesRouteItemService;

    @GET
    @Path("/getPage")
    public R<PageResult<DronesRouteItem>> getPage(@BeanParam DronesRouteItemQueryDto dto,
            @BeanParam PageRequest pageRequest) {
        return R.ok(dronesRouteItemService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<DronesRouteItem>> getByDto(@BeanParam DronesRouteItemQueryDto dto) {
        return R.ok(dronesRouteItemService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<DronesRouteItem>> getAll() {
        return R.ok(dronesRouteItemService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> create(DronesRouteItem entity) {
        return R.ok(dronesRouteItemService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<Void> update(DronesRouteItemDto dto) {
        dronesRouteItemService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        DronesRouteItem entity = DronesRouteItem.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}