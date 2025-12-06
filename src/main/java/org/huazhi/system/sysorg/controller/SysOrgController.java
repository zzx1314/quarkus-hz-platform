package org.huazhi.system.sysorg.controller;

import java.util.List;

import org.huazhi.system.sysorg.entity.SysOrg;
import org.huazhi.system.sysorg.entity.dto.SysOrgQueryDto;
import org.huazhi.system.sysorg.entity.dto.SysOrgTreeDto;
import org.huazhi.system.sysorg.service.SysOrgService;
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

@Path("/sysOrg")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SysOrgController {
    @Inject
    SysOrgService sysOrgService;

    @GET
    @Path("/tree")
    public R<Object> tree(@BeanParam SysOrgQueryDto sysOrgDto) {
        return R.ok(sysOrgService.listOrgTrees(sysOrgDto));
    }

    @GET
    @Path("/getByDto")
    public R<List<SysOrg>> getByDto(@BeanParam SysOrgQueryDto sysOrgDto) {
        return R.ok(sysOrgService.listOrgsByDto(sysOrgDto));
    }

    @GET
    @Path("/getAll")
    public R<List<SysOrg>> getAll() {
        return R.ok(sysOrgService.listOrgs());
    }

    @GET
    @Path("/allList")
    public R<Object> allList(@BeanParam SysOrgQueryDto queryDto) {
        return R.ok(sysOrgService.listAllOrgVo(queryDto));
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Object> createOrg(SysOrgTreeDto sysOrg) {
        return sysOrgService.registerOrg(sysOrg);
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<Object> update(SysOrgTreeDto sysOrg) {
        return sysOrgService.updateOrg(sysOrg);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        SysOrg entity = SysOrg.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}
