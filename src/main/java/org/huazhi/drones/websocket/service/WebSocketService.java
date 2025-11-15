package org.huazhi.drones.websocket.service;

import java.util.Map;

import org.huazhi.drones.websocket.entity.MessageHeartbeat;
import org.huazhi.drones.websocket.entity.MessageInfo;
import org.huazhi.exception.BusinessException;
import org.huazhi.util.JsonUtil;

import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.PathParam;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * websocket服务
 * ws://192.168.41.227:8080/api/notice/9527/a2ed64b9095b4def83afcab1d4ca5a72
 * 心跳响应：
    {
    "deviceId": "9527",
    "type": "heartbeat",
    "status": "success",
    "speed": 4km/h,
    "height": 1000m,
    "battery": 100%,
    "course": 0°,
    "location": "116.397428,39.90923"
    }
 */
@Slf4j
@WebSocket(path = "/notice/{deviceId}/{accessToken}")
public class WebSocketService {

    @Inject
    ConnectionManager connectionManager;

    @Inject
    BusService busService;

    @OnOpen(broadcast = true)
    public void onOpen(WebSocketConnection connection, @PathParam("deviceId") String deviceId,
            @PathParam("accessToken") String accessToken) {
        log.info("WebSocket opened for deviceId: {}, accessToken: {}", deviceId, accessToken);
        if (!busService.checkClientId(deviceId)) {
            log.info("客户端" + deviceId + "客户端id验证失败");
            throw new BusinessException("客户端" + deviceId + "客户端未注册");
        }
        if (!busService.checkToken(deviceId, accessToken)) {
            log.info("用户" + deviceId + "token验证失败");
            throw new BusinessException("用户" + deviceId + "token验证失败");
        }
        connectionManager.setAttribute(deviceId, "accessToken", accessToken);
        connectionManager.addConnection(deviceId, connection);
    }

    @OnClose
    public void onClose(@PathParam("deviceId") String deviceId) {
        log.info("WebSocket closed for deviceId: {}", deviceId);
        connectionManager.removeConnection(deviceId);
        busService.closeConnect(deviceId);
    }

    @OnTextMessage(broadcast = true)
    public void onMessage(String message, @PathParam("deviceId") String deviceId, WebSocketConnection connection) {
        log.info("Received message from deviceId {}: {}", deviceId, message);
        Map<String, Object> messageJson = JsonUtil.fromJsonToMap(message);
        if (messageJson.get("type").equals("heartbeat")) {
            // 心跳消息
            MessageHeartbeat heartbeat = JsonUtil.fromJson(message, MessageHeartbeat.class);
            MessageInfo messageInfo = busService.reportStatus(heartbeat);
            // 发送心跳响应
            connectionManager.sendHeartBeatReturn(heartbeat.getDeviceId(), messageInfo);
        } else if (messageJson.get("type").equals("report")) {
            // 处理指令结果
            MessageInfo messageInfo = JsonUtil.fromJson(message, MessageInfo.class);
            busService.updateCommandReport(messageInfo.getId(), messageInfo.getStatus(), messageInfo.getReturnValue());
        }
    }
}
