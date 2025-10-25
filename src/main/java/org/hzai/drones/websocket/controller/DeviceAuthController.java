package org.hzai.drones.websocket.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hzai.drones.device.entity.DronesDevice;
import org.hzai.drones.device.entity.dto.DronesDeviceDto;
import org.hzai.drones.device.entity.dto.DronesDeviceQueryDto;
import org.hzai.drones.device.service.DronesDeviceService;
import org.hzai.drones.util.CheckClientTokenUtil;
import org.hzai.util.R;
import org.hzai.util.RedisUtil;

import io.quarkus.runtime.util.StringUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/client")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceAuthController {

    @Inject
    RedisUtil redisUtil;

    @Inject
    CheckClientTokenUtil checkClientTokenUtil;

    @Inject
    DronesDeviceService dronesDeviceService;

    /**
     * 客户端认证接口
     */
    @GET
    @Path("clientAuth/{clientId}")
    public R<String> clientAuth(@PathParam("clientId") String clientId) {
        log.info("clientId:{}", clientId);
        redisUtil.del(clientId);
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace("-", "");
        redisUtil.set(uuidStr, clientId, 60 * 10);
        return R.ok(uuidStr, "success");
    }

    /**
     * 设备注册接口
     * @param deviceDto
     * @param authToken
     * @return
     */
    @POST
    @Path("register")
    public R<String> register(DronesDeviceDto deviceDto, @HeaderParam("Authorization") String authToken) {
        log.info("device register:{}, {}", deviceDto, authToken);
        // 设备是否通过认证，如果认证通过可以进行
        boolean checkRes = checkClientTokenUtil.checkToken(deviceDto.getDeviceId(), authToken);
        if (!checkRes){
            return R.failed("无效的token信息");
        }
        registerDevice(deviceDto);
        return R.ok("设备注册成功");
    }

    private void registerDevice(DronesDeviceDto deviceDto) {
        DronesDeviceQueryDto queryDto = new DronesDeviceQueryDto();
        queryDto.setDeviceId(deviceDto.getDeviceId());
        DronesDevice existDevice = dronesDeviceService.listOne(queryDto);
        // 处理设备型号
        handleDeviceMode(deviceDto);
        // 查询设备是否存在, 不存在则注册
        if (existDevice == null) {
            deviceDto.setStatus("已注册");
            dronesDeviceService.registerByDto(deviceDto);
        } else {
            // 设备已存在，更新最后上线时间
            deviceDto.setId(existDevice.getId());
            deviceDto.setCommTime(LocalDateTime.now());
            dronesDeviceService.replaceByDto(deviceDto);
        }

    }

    /**
     * 处理设备型号
     * @param device
     */
    private void handleDeviceMode(DronesDeviceDto device) {
        if (StringUtil.isNullOrEmpty(device.getDeviceId()) && device.getDeviceId().length() >= 5) {
            // 获取第4到5位字符
            String deviceMode = device.getDeviceId().substring(3, 5);
            if (deviceMode.equals("00")) {
                device.setDeviceType("310B");
            } else if (deviceMode.equals("01")) {
                device.setDeviceType("310P");
            } else if (deviceMode.equals("10")) {
                device.setDeviceType("3403");
            } else if (deviceMode.equals("20")) {
                device.setDeviceType("RK3588");
            } else if (deviceMode.equals("21")) {
                device.setDeviceType("RK3568");
            } else if (deviceMode.equals("22")) {
                device.setDeviceType("RK3576");
            } else if (deviceMode.equals("30")) {
                device.setDeviceType("920");
            } else if (deviceMode.equals("40")) {
                device.setDeviceType("3330E");
            }
        }
    }
}
