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
import org.jboss.logging.Logger;

import io.quarkus.websockets.next.OnBinaryMessage;
import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.PathParam;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.inject.Inject;

/**
 * websocket服务
 */
@WebSocket(path = "/notice/{deviceId}/{accessToken}")
public class WebSocketService {
    private static final Logger log = Logger.getLogger(WebSocketService.class);

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
        log.infof("WebSocket opened for deviceId: %s, accessToken: %s", deviceId, accessToken);
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
        log.infof("WebSocket closed for deviceId: %s", deviceId);
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
        log.infof("Received message from deviceId %s: %s", deviceId, message);
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
                    log.infof("Device %s disconnected.", deviceId);
                }
                break;

            case "file_start":
                String streamId = (String) messageJson.get("streamId");
                deviceCurrentStream.put(deviceId, streamId);
                streamBuffers.put(streamId, new ByteArrayOutputStream());
                log.infof("Device %s started file stream %s", deviceId, streamId);
                break;

            case "file_end":
                streamId = (String) messageJson.get("streamId");
                String targetIds = (String) messageJson.get("targetIds");
                ByteArrayOutputStream out = streamBuffers.remove(streamId);
                if (out != null) {
                    saveFile(out.toByteArray(), deviceId, streamId, targetIds);
                    log.infof("Device %s finished file stream %s", deviceId, streamId);
                }
                deviceCurrentStream.remove(deviceId);
                break;
            case "server-command":
                DronesCommandReturn commandReturn = JsonUtil.fromJson(message, DronesCommandReturn.class);
                String params = commandReturn.getParams().toString();
                busService.updateCommandReport(commandReturn.getCommandId(), commandReturn.getStatus(), params);
                break;
            case "report":
                MessageInfo messageInfo = JsonUtil.fromJson(message, MessageInfo.class);
                busService.updateCommandReport(messageInfo.getId(), messageInfo.getStatus(), messageInfo.getReturnValue());
                break;

            case "torosReport":
                log.infof("处理torosReport %s: %s", deviceId, message);
                busService.saveCommandReport(Long.valueOf(messageJson.get("missionId").toString()), message);
                break;

            default:
                log.warnf("Unknown text message type: %s", type);
        }
    }

    @OnBinaryMessage
    public void onBinaryMessage(byte[] data, @PathParam("deviceId") String deviceId,
                                WebSocketConnection connection) {
        String streamId = deviceCurrentStream.get(deviceId);
        if (streamId == null) {
            log.warnf("No active stream for device %s, discard data", deviceId);
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
        log.infof("Received %d bytes for device %s stream %s", data.length, deviceId, streamId);
    }

    private void saveFile(byte[] bytes, String deviceId, String streamId, String targetIds) {
        try {
            Path dirPath = Paths.get(fileConfig.basePath() + "/image");
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath); // 创建父目录
            }

            Path filePath = dirPath.resolve(deviceId + "_" + streamId + ".jpg");
            Files.write(filePath, bytes, StandardOpenOption.CREATE);

            // 实时广播给其他客户端
            TrackWebSocket.broadcast(bytes, targetIds);
            log.infof("Saved file: %s", filePath.toString());
        } catch (IOException e) {
            log.errorf("Failed to save file for device %s stream %s", deviceId, streamId, e);
        }
    }

}

