package org.huazhi.drones.websocket.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import lombok.extern.slf4j.Slf4j;

import org.huazhi.drones.command.service.DronesCommandService;
import org.huazhi.drones.device.entity.DronesDevice;
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
    public R<Void> start(@QueryParam("deviceId") Long deviceId) {
        DronesDevice device = deviceService.listById(deviceId);
        log.info("start ImageReceiver--deviceId=" + device.getDeviceId());
        pool.start(device.getDeviceIp(), 9650);
        return R.ok();
    }

    @GET
    @Path("/stop")
    public R<Void> stop(@QueryParam("deviceId") Long deviceId) {
         DronesDevice device = deviceService.listById(deviceId);
        log.info("start ImageReceiver--deviceId=" + device.getDeviceId());
        pool.stop(device.getDeviceIp(), 9650);
        return R.ok();
    }
}