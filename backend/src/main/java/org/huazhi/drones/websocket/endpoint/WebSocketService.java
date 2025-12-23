package org.huazhi.drones.websocket.endpoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.huazhi.config.FileConfig;
import org.huazhi.drones.business.command.entity.dto.DronesCommandReturn;
import org.huazhi.drones.websocket.entity.MessageHeartbeat;
import org.huazhi.drones.websocket.entity.MessageInfo;
import org.huazhi.drones.websocket.service.BusService;
import org.huazhi.drones.websocket.service.ConnectionManager;
import org.huazhi.exception.BusinessException;
import org.huazhi.util.JsonUtil;

import io.quarkus.websockets.next.OnBinaryMessage;
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
 */
@Slf4j
@WebSocket(path = "/notice/{deviceId}/{accessToken}")
public class WebSocketService {

    @Inject
    ConnectionManager connectionManager;

    @Inject
    BusService busService;

    @Inject
    FileConfig fileConfig;

    // 存储每个流的临时数据
    private final Map<String, ByteArrayOutputStream> streamBuffers = new ConcurrentHashMap<>();
    // 设备当前活跃 streamId
    private final Map<String, String> deviceCurrentStream = new ConcurrentHashMap<>();

    @OnOpen(broadcast = true)
    public void onOpen(WebSocketConnection connection, @PathParam("deviceId") String deviceId,
                       @PathParam("accessToken") String accessToken) {
        log.info("WebSocket opened for deviceId: {}, accessToken: {}", deviceId, accessToken);
        if (!busService.checkClientId(deviceId)) {
            throw new BusinessException("客户端" + deviceId + "客户端未注册");
        }
        if (!busService.checkToken(deviceId, accessToken)) {
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
        // 清理该设备的活跃流
        String streamId = deviceCurrentStream.remove(deviceId);
        if (streamId != null) {
            streamBuffers.remove(streamId);
        }
    }

    @OnTextMessage(broadcast = true)
    public void onMessage(String message, @PathParam("deviceId") String deviceId,
                          WebSocketConnection connection) {
        log.info("Received message from deviceId {}: {}", deviceId, message);
        Map<String, Object> messageJson = JsonUtil.fromJsonToMap(message);

        String type = (String) messageJson.get("type");

        switch (type) {
            case "heartbeat":
                MessageHeartbeat heartbeat = JsonUtil.fromJson(message, MessageHeartbeat.class);
                heartbeat.setDeviceId(deviceId);
                if (connectionManager.isAlive(deviceId)) {
                    MessageInfo info = busService.reportStatus(heartbeat);
                    connectionManager.sendHeartBeatReturn(deviceId, info);
                } else {
                    log.info("Device {} disconnected.", deviceId);
                }
                break;

            case "file_start":
                String streamId = (String) messageJson.get("streamId");
                deviceCurrentStream.put(deviceId, streamId);
                streamBuffers.put(streamId, new ByteArrayOutputStream());
                log.info("Device {} started file stream {}", deviceId, streamId);
                break;

            case "file_end":
                streamId = (String) messageJson.get("streamId");
                ByteArrayOutputStream out = streamBuffers.remove(streamId);
                if (out != null) {
                    saveFile(out.toByteArray(), deviceId, streamId);
                    log.info("Device {} finished file stream {}", deviceId, streamId);
                }
                deviceCurrentStream.remove(deviceId);
                break;
            case "service":
                DronesCommandReturn commandReturn = JsonUtil.fromJson(message, DronesCommandReturn.class);
                String params = commandReturn.getParams().toString();
                busService.updateCommandReport(commandReturn.getCommandId(), commandReturn.getStatus(), params);
                break;
            case "report":
                MessageInfo messageInfo = JsonUtil.fromJson(message, MessageInfo.class);
                busService.updateCommandReport(messageInfo.getId(), messageInfo.getStatus(), messageInfo.getReturnValue());
                break;

            case "torosReport":
                log.info("处理torosReport {}: {}", deviceId, message);
                busService.saveCommandReport(Long.valueOf(messageJson.get("missionId").toString()), message);
                break;

            default:
                log.warn("Unknown text message type: {}", type);
        }
    }

    @OnBinaryMessage
    public void onBinaryMessage(byte[] data, @PathParam("deviceId") String deviceId,
                                WebSocketConnection connection) {
        String streamId = deviceCurrentStream.get(deviceId);
        if (streamId == null) {
            log.warn("No active stream for device {}, discard data", deviceId);
            return;
        }

        ByteArrayOutputStream out = streamBuffers.get(streamId);
        if (out != null) {
            try {
                out.write(data);
            } catch (IOException e) {
                log.error("Failed to append data to stream {}", streamId, e);
            }
        }
        log.info("Received {} bytes for device {} stream {}", data.length, deviceId, streamId);
    }

    private void saveFile(byte[] bytes, String deviceId, String streamId) {
        try {
            Path dirPath = Paths.get(fileConfig.basePath() + "/image");
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath); // 创建父目录
            }

            Path filePath = dirPath.resolve(deviceId + "_" + streamId + ".jpg");
            Files.write(filePath, bytes, StandardOpenOption.CREATE);

            // 可选：实时广播给其他客户端
            TrackWebSocket.broadcast(bytes);
            log.info("Saved file: {}", filePath.toString());
        } catch (IOException e) {
            log.error("Failed to save file for device {} stream {}", deviceId, streamId, e);
        }
    }

}

