package org.huazhi.drones.websocket.service;

import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.huazhi.drones.command.entity.webscoketdto.DronesCommandWebsocketV1;
import org.huazhi.drones.websocket.entity.MessageInfo;
import org.huazhi.util.JsonUtil;

@Slf4j
@ApplicationScoped
public class ConnectionManager {
    @Inject
    BusService busService;
    // deviceId -> WebSocketConnection
    private final ConcurrentHashMap<String, WebSocketConnection> connections = new ConcurrentHashMap<>();
    // 保存 deviceId -> 自定义属性
    private final Map<String, Map<String, Object>> attributes = new ConcurrentHashMap<>();

    public void addConnection(String deviceId, WebSocketConnection connection) {
        connections.put(deviceId, connection);
        attributes.put(deviceId, new ConcurrentHashMap<>());
    }

    public void removeConnection(String deviceId) {
        connections.remove(deviceId);
        attributes.remove(deviceId);
    }

    public WebSocketConnection getConnection(String deviceId) {
        return connections.get(deviceId);
    }

    public void setAttribute(String deviceId, String key, Object value) {
        attributes.computeIfAbsent(deviceId, k -> new ConcurrentHashMap<>()).put(key, value);
    }

    public Object getAttribute(String deviceId, String key) {
        Map<String, Object> map = attributes.get(deviceId);
        return map != null ? map.get(key) : null;
    }

    /**
     * 发送心跳响应
     */
    public void sendHeartBeatReturn(String deviceId, MessageInfo message) {
        WebSocketConnection conn = connections.get(deviceId);
        if (conn != null && conn.isOpen()) {
            String jsonString = JsonUtil.toJson(message);
            log.info("Sending heartbeat response to deviceId {}: {}", deviceId, jsonString);
            conn.sendTextAndAwait(jsonString);
        } else {
            log.warn("Connection for deviceId {} is not available.", deviceId);
        }
    }

    /**
     * 通过deviceId发送消息
     */
    public void sendMessageByDeviceId(String deviceId, DronesCommandWebsocketV1 message) {
        WebSocketConnection conn = connections.get(deviceId);
        if (conn != null && conn.isOpen()) {
            // 保存指令并获取指令ID
            Long id = busService.saveCommand(message);
            message.setMissionId(id);
            String jsonString = JsonUtil.toJson(message);
            log.info("Sending message to deviceId {}: {}", deviceId, jsonString);
            conn.sendTextAndAwait(jsonString);
        } else {
            log.warn("Connection for deviceId {} is not available.", deviceId);
        }
    }
}
