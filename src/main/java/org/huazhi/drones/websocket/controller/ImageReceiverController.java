package org.huazhi.drones.websocket.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.huazhi.drones.command.entity.DronesCommand;
import org.huazhi.drones.command.entity.dto.DronesCommandQueryDto;
import org.huazhi.drones.command.service.DronesCommandService;
import org.huazhi.drones.device.entity.DronesDevice;
import org.huazhi.drones.device.entity.dto.DronesDeviceQueryDto;
import org.huazhi.drones.device.service.DronesDeviceService;
import org.huazhi.drones.websocket.service.ImageReceiverPool;
import org.huazhi.util.R;

@Path("/imageReceiver")
@Slf4j
public class ImageReceiverController {

    @Inject
    ImageReceiverPool pool;

    @Inject
    DronesCommandService commandService;

    @Inject
    DronesDeviceService deviceService;

    @GET
    @Path("/start")
    public R<Void> start(@QueryParam("taskId") Long taskId) {
        List<DronesCommand> commands = commandService.listEntitysByDto(new DronesCommandQueryDto().setTaskId(taskId));
        if (!commands.isEmpty()) {
            DronesCommand command  = commands.getFirst();
            DronesDevice device = deviceService.listOne(new DronesDeviceQueryDto().setDeviceId(command.getDeviceId()));
            log.info("start ImageReceiver--deviceId=" + device.getDeviceId());
            pool.start(device.getDeviceIp(), 9650);
        }
        return R.ok();
    }

    @GET
    @Path("/stop")
    public R<Void> stop(@QueryParam("taskId") Long taskId) {
        List<DronesCommand> commands = commandService.listEntitysByDto(new DronesCommandQueryDto().setTaskId(taskId));
        if (!commands.isEmpty()) {
            DronesCommand command  = commands.getFirst();
            DronesDevice device = deviceService.listOne(new DronesDeviceQueryDto().setDeviceId(command.getDeviceId()));
            log.info("stop ImageReceiver--deviceId=" + device.getDeviceId());
            pool.start(device.getDeviceIp(), 9650);
        }
        return R.ok();
    }
}