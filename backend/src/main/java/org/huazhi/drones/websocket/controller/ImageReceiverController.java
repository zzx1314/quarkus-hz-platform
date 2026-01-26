package org.huazhi.drones.websocket.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

import org.huazhi.drones.business.command.service.DronesCommandService;
import org.huazhi.drones.business.device.entity.DronesDevice;
import org.huazhi.drones.business.device.service.DronesDeviceService;
import org.huazhi.drones.websocket.service.ImageReceiverPool;
import org.huazhi.util.R;
import org.jboss.logging.Logger;

@Path("/imageReceiver")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImageReceiverController {
     private static final Logger log = Logger.getLogger(ImageReceiverController.class);

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

    @POST
    @Path("/stop")
    public R<Void> stop(Map<String, Long> body) {
        log.info("stop ImageReceiver--deviceId=" + body.get("deviceId"));
        Long deviceId = body.get("deviceId");
        DronesDevice device = deviceService.listById(deviceId);
        log.info("stop ImageReceiver--deviceId=" + device.getDeviceId());
        pool.stop(device.getDeviceIp(), 9650);
        return R.ok();
    }
}