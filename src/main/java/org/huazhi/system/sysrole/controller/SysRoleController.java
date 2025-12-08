package org.huazhi.system.sysrole.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.huazhi.system.sysorg.entity.SysOrg;
import org.huazhi.system.sysrole.entity.SysRole;
import org.huazhi.system.sysrole.entity.dto.SysRoleDto;
import org.huazhi.system.sysrole.entity.dto.SysRoleQueryDto;
import org.huazhi.system.sysrole.entity.mapper.SysRoleMapper;
import org.huazhi.system.sysrole.service.SysRoleService;
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

@Path("/sysRole")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SysRoleController {
    @Inject
    SysRoleMapper sysRoleMapper;

    @Inject
    SysRoleService sysRoleService;

    @GET
    @Path("/getPage")
    public R<PageResult<SysRole>> getPage(@BeanParam SysRoleQueryDto sysRoleDto, @BeanParam PageRequest pageRequest) {
        return R.ok(sysRoleService.listRolePage(sysRoleDto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<SysRole>> getByDto(@BeanParam SysRoleQueryDto sysRoleDto) {
        return R.ok(sysRoleService.listRolesByDto(sysRoleDto));
    }

    @GET
    @Path("/getAll")
    public R<List<SysRole>> getAll() {
        return R.ok(sysRoleService.listRoles());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Long> createRole(SysRole sysRole) {
        return R.ok(sysRoleService.registerRole(sysRole));
    }

    @PUT
    @Path("/update")
    @Transactional
    public R<SysRole> update(SysRoleDto sysRoleDto) {
        SysRole entity = SysRole.findById(sysRoleDto.getId());
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setUpdateTime(LocalDateTime.now());
        sysRoleMapper.updateEntityFromDto(sysRoleDto, entity);
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
