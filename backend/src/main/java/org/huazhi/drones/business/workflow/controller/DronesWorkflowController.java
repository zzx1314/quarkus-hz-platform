package org.huazhi.drones.business.workflow.controller;

import java.util.List;

import org.huazhi.drones.business.routelibrary.entity.vo.DronesRouteLibraryVo;
import org.huazhi.drones.business.workflow.entity.DronesWorkflow;
import org.huazhi.drones.business.workflow.entity.dto.DronesWorkflowDto;
import org.huazhi.drones.business.workflow.entity.dto.DronesWorkflowQueryDto;
import org.huazhi.drones.business.workflow.service.DronesWorkflowService;
import org.huazhi.drones.business.workflow.vo.DronesWorkflowVo;
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

@Path("/dronesWorkflow")
@TokenRequired
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesWorkflowController {
    @Inject
    DronesWorkflowService dronesWorkflowService;

    @GET
    @Path("/getPage")
    public R<PageResult<DronesWorkflow>> getPage(@BeanParam DronesWorkflowQueryDto dto,
            @BeanParam PageRequest pageRequest) {
        return R.ok(dronesWorkflowService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<DronesWorkflow>> getByDto(@BeanParam DronesWorkflowQueryDto dto) {
        return R.ok(dronesWorkflowService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getWorkflowVoByTaskId/{taskId}")
    public R<DronesWorkflowVo> getWorkflowVoByTaskId(@PathParam("taskId") Long taskId) {
        return R.ok(dronesWorkflowService.getWorkflowGraph(taskId));
    }

    @GET
    @Path("/getWorkflowByTaskId/{taskId}")
    public R<DronesWorkflow> getWorkflowByTaskId(@PathParam("taskId") Long taskId) {
        return R.ok(dronesWorkflowService.getWorkflow(taskId));
    }

    @GET
    @Path("/getCommandJsonString/{taskId}")
    public R<String> getCommandJsonString(@PathParam("taskId") Long taskId) {
        return R.ok(dronesWorkflowService.getCommandJsonString(taskId));
    }

    @GET
    @Path("/getRouteByTaskId/{taskId}")
    public R<List<DronesRouteLibraryVo>> getRouteByTaskId(@PathParam("taskId") Long taskId) {
        return R.ok(dronesWorkflowService.getRouteByTaskId(taskId));
    }

    @GET
    @Path("/getAll")
    public R<List<DronesWorkflow>> getAll() {
        return R.ok(dronesWorkflowService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Long> create(DronesWorkflow entity) {
        return R.ok(dronesWorkflowService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<Void> update(DronesWorkflowDto dto) {
        DronesWorkflowQueryDto queryDto = new DronesWorkflowQueryDto();
        queryDto.setTaskId(dto.getTaskId());
        dronesWorkflowService.replaceByDto(queryDto, dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        DronesWorkflow entity = DronesWorkflow.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

}