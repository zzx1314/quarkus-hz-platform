package org.hzai.system.sysorg.controller;

import java.util.List;

import org.hzai.system.sysorg.entity.SysOrg;
import org.hzai.system.sysorg.entity.dto.SysOrgDto;
import org.hzai.system.sysorg.entity.dto.SysOrgQueryDto;
import org.hzai.system.sysorg.entity.mapper.SysOrgMapper;
import org.hzai.system.sysorg.service.SysOrgService;
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

@Path("/sysOrg")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SysOrgController {
    @Inject
    SysOrgMapper sysOrgMapper;

    @Inject
    SysOrgService sysOrgService;


     @GET
    @Path("/getPage")
    public R<PageResult<SysOrg>> getPage(@BeanParam SysOrgQueryDto sysOrgDto, @BeanParam PageRequest pageRequest) {
        return R.ok(sysOrgService.listOrgsPage(sysOrgDto, pageRequest));
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

    @POST
    @Path("/create")
    @Transactional
    public R<Boolean> createOrg(SysOrg sysOrg) {
        return R.ok(sysOrgService.registerOrg(sysOrg));
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<SysOrg> update(SysOrgDto sysOrg) {
        SysOrg entity = SysOrg.findById(sysOrg.getId());
        if(entity == null) {
            throw new NotFoundException();
        }
        sysOrgMapper.updateEntityFromDto(sysOrg, entity);
        return R.ok(entity);
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
