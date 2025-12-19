package org.huazhi.drones.task.controller;

import java.util.List;

import org.huazhi.drones.task.entity.DronesTask;
import org.huazhi.drones.task.entity.dto.DronesTaskDto;
import org.huazhi.drones.task.entity.dto.DronesTaskQueryDto;
import org.huazhi.drones.task.entity.vo.DronesTaskStatusVo;
import org.huazhi.drones.task.service.DronesTaskService;
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

@Path("/dronesTask")
@TokenRequired
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DronesTaskController {
    @Inject
    DronesTaskService dronesTaskService;

    @GET
    @Path("/getPage")
    public R<PageResult<DronesTask>> getPage(@BeanParam DronesTaskQueryDto dto, @BeanParam PageRequest pageRequest) {
        return R.ok(dronesTaskService.listPage(dto, pageRequest));
    }

    @GET
    @Path("/getByDto")
    public R<List<DronesTask>> getByDto(@BeanParam DronesTaskQueryDto dto) {
        return R.ok(dronesTaskService.listEntitysByDto(dto));
    }

    @GET
    @Path("/getAll")
    public R<List<DronesTask>> getAll() {
        return R.ok(dronesTaskService.listEntitys());
    }

    @POST
    @Path("/create")
    @Transactional
    public R<Long> create(DronesTask entity) {
        return R.ok(dronesTaskService.register(entity));
    }

    @PUT
    @Path("/update")
    public R<Void> update(DronesTaskDto dto) {
        dronesTaskService.replaceByDto(dto);
        return R.ok();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public R<Void> delete(@PathParam("id") Long id) {
        DronesTask entity = DronesTask.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setIsDeleted(1);
        entity.persist();
        return R.ok();
    }

    /**
     * 启动任务
     */
    @GET
    @Path("/startTask/{id}")
    public R<Void> startTask(@PathParam("id") Long id) {
        dronesTaskService.startTask(id);
        return R.ok();
    }

    /**
     * 查看任务状态
     */
    @GET
    @Path("/getTaskStatus/{id}")
    public R<DronesTaskStatusVo> getTaskStatus(@PathParam("id") Long id) {
        return R.ok(dronesTaskService.getTaskStatus(id));  
    }

}