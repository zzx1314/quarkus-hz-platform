package org.hzai.drones.websocket.service;

import java.time.LocalDateTime;

import org.hzai.drones.command.entity.DronesCommand;
import org.hzai.drones.command.entity.dto.DronesCommandDto;
import org.hzai.drones.command.service.DronesCommandService;
import org.hzai.drones.device.entity.DronesDevice;
import org.hzai.drones.device.entity.dto.DronesDeviceDto;
import org.hzai.drones.device.entity.dto.DronesDeviceQueryDto;
import org.hzai.drones.device.service.DronesDeviceService;
import org.hzai.drones.websocket.entity.MessageInfo;
import org.hzai.util.RedisUtil;
import org.jose4j.json.internal.json_simple.JSONObject;

import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
    public MessageInfo reportStatus(DronesDeviceDto dto) {
        dto.setCommTime(LocalDateTime.now());
        if ("success".equals(dto.getStatus())) {
            dto.setStatus("在线");
        } else if ("error".equals(dto.getStatus())) {
            dto.setStatus("异常");
        }
        dronesDeviceService.replaceByDto(dto);
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
    public Long saveCommand(MessageInfo messageInfo){
        DronesCommand command = new DronesCommand();
        command.setCommandParams(JSONObject.toJSONString(messageInfo.getData()));
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
