package org.huazhi.drones.websocket.service;

import java.time.LocalDateTime;

import org.huazhi.drones.command.entity.DronesCommand;
import org.huazhi.drones.command.entity.dto.DronesCommandDto;
import org.huazhi.drones.command.entity.webscoketdto.DronesCommandWebsocketV1;
import org.huazhi.drones.command.service.DronesCommandService;
import org.huazhi.drones.device.entity.DronesDevice;
import org.huazhi.drones.device.entity.dto.DronesDeviceDto;
import org.huazhi.drones.device.entity.dto.DronesDeviceQueryDto;
import org.huazhi.drones.device.service.DronesDeviceService;
import org.huazhi.drones.websocket.entity.MessageHeartbeat;
import org.huazhi.drones.websocket.entity.MessageInfo;
import org.huazhi.util.JsonUtil;
import org.huazhi.util.RedisUtil;

import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class BusService {
    @Inject
    RedisUtil redisUtil;

    @Inject
    DronesDeviceService dronesDeviceService;

    @Inject
    DronesCommandService dronesCommandService;

    /**
     * 检查客户端id
     */
    @Transactional
    public boolean checkClientId(String clientId) {
        DronesDeviceQueryDto queryDto = new DronesDeviceQueryDto();
        queryDto.setDeviceId(clientId);
        DronesDevice findDev = dronesDeviceService.listOne(queryDto);
        if (findDev == null){
            return false;
        }
        return true;
    }

    /**
     * 检查token信息
     */
    public boolean checkToken(String clientId, String token) {
        String auth = redisUtil.get(token);
        if (StringUtil.isNullOrEmpty(auth) || !clientId.equals(auth)){
            return false;
        } else {
            redisUtil.expire(token, 60 * 10);
        }
        return true;
    }

    /**
     * 设备状态上报
     * @param dto
     * @return
     */
    public MessageInfo reportStatus(MessageHeartbeat messageHeartbeat) {
        DronesDeviceDto dto = new DronesDeviceDto();
        dto.setCommTime(LocalDateTime.now());
        dto.setDeviceId(messageHeartbeat.getDeviceId());
        if (StringUtil.isNullOrEmpty(messageHeartbeat.getStatus())) {
            dto.setStatus("success");
        }
        if (messageHeartbeat.getDrones() != null) {
            dto.setSpeed(messageHeartbeat.getDrones().getSpeed());
            dto.setHeight(messageHeartbeat.getDrones().getHeight());
            dto.setBattery(messageHeartbeat.getDrones().getBattery());
            dto.setCourse(messageHeartbeat.getDrones().getCourse());
            dto.setLocation(messageHeartbeat.getDrones().getLocation());
        }
        if ("success".equals(dto.getStatus())) {
            dto.setStatus("在线");
        } else if ("error".equals(dto.getStatus())) {
            dto.setStatus("异常");
        }
        DronesDeviceQueryDto queryDto = new DronesDeviceQueryDto();
        queryDto.setDeviceId(dto.getDeviceId());
        dronesDeviceService.replaceByQuery(dto, queryDto);
        return new MessageInfo("report", "success");
    }

    /**
     * 断开连接
     */
    public void closeConnect(String clientId){
        DronesDeviceDto data = new DronesDeviceDto();
        data.setStatus("心跳丢失");
        DronesDeviceQueryDto queryDto = new DronesDeviceQueryDto();
        queryDto.setDeviceId(clientId);
        dronesDeviceService.replaceByQuery(data, queryDto);
    }

    /**
     * 保存指令消息
     */
    public Long saveCommand(DronesCommandWebsocketV1 messageInfo){
        DronesCommand command = new DronesCommand();
        command.setCommandParams(JsonUtil.toJson(messageInfo));
        command.setStatus("已下发");
        command.setCreateTime(LocalDateTime.now());
        command.setUpdateTime(LocalDateTime.now());
        dronesCommandService.register(command);
        return command.getId();
    }


    /**
     * 更新指令执行结果
     */
    public void updateCommandReport(Long id, String status, String returnValue){
        if ("success".equals(status)) {
            status = "执行成功";
        } else if ("error".equals(status)) {
            status = "执行失败";
        }
        DronesCommandDto dto = new DronesCommandDto();
        dto.setId(id);
        dto.setStatus(status);
        dto.setReturnValue(returnValue);
        dronesCommandService.replaceByDto(dto);
    }


}
