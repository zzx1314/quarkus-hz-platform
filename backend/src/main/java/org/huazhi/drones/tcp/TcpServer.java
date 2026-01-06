package org.huazhi.drones.tcp;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.huazhi.config.FileConfig;
import org.huazhi.drones.business.command.entity.tcpdto.TcpConnectCommand;
import org.huazhi.drones.business.model.entity.DronesModel;
import org.huazhi.drones.business.model.service.DronesModelService;
import org.huazhi.util.JsonUtil;

import io.quarkus.runtime.StartupEvent;

@Slf4j
@ApplicationScoped
public class TcpServer {
    @Inject
    DronesModelService dronesModelService;

    @Inject
    FileConfig fileConfig;

    private static final int PORT = 9000;
    private ExecutorService executor = Executors.newCachedThreadPool();

    public void start(@Observes StartupEvent ev) {
        executor.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                log.info("TCP Server started on port " + PORT);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    executor.submit(() -> handleClient(clientSocket));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleClient(Socket socket) {
        log.info("Client connected: " + socket.getRemoteSocketAddress());
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            boolean running = true;
            while (running) {
                // 1. 读取 Header
                byte[] headerBytes = in.readNBytes(FrameHeader.HEADER_LENGTH);
                FrameHeader header = FrameHeader.fromBytes(headerBytes);

                // 2. 读取 Body
                byte[] bodyBytes = in.readNBytes(header.getLength());

                switch (header.getFrameType()) {
                    case 0: // INIT
                        handleInit(bodyBytes);
                        break;
                    case 1: // MESSAGE
                        handleMessage(bodyBytes);
                        break;
                    case 2: // FILE_DATA
                        handleFileData(bodyBytes);
                        break;
                    case 3: // FINISH
                        handleFinish(bodyBytes);
                        running = false; // 结束 TCP 会话
                        break;
                    case 4: // ERROR
                        handleError(bodyBytes);
                        running = false;
                        break;
                    default:
                        log.info("Unknown frameType: " + header.getFrameType());
                        running = false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                log.info("Client disconnected: " + socket.getRemoteSocketAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ---------------- 业务处理方法 ----------------
    private void handleInit(byte[] body) {
        String json = new String(body, StandardCharsets.UTF_8);
        log.info("INIT received: " + json);
        // 验证签名
    }

    private void handleMessage(byte[] body) {
        String msg = new String(body, StandardCharsets.UTF_8);
        log.info("MESSAGE received: " + msg);
    }

    private void handleFileData(byte[] body) throws IOException {
        // 当前简单写到本地 temp 文件
        try (FileOutputStream fos = new FileOutputStream(fileConfig.basePath() + "/temp/received_file.tmp", true)) {
            fos.write(body);
        }
        log.info("FILE_DATA received: " + body.length + " bytes");
    }

    private void handleFinish(byte[] body) {
        String json = new String(body, StandardCharsets.UTF_8);
        log.info("FINISH received: " + json);
        // 标记任务完成
        TcpConnectCommand tcpConnectCommand = JsonUtil.fromJson(json, TcpConnectCommand.class);
        handleUplodModeFile(tcpConnectCommand);
    }

    private void handleUplodModeFile(TcpConnectCommand tcpConnectCommand){
        if (tcpConnectCommand.getTask().getTaskType().equals("FILE_MAP_MODEL_UPLOAD")
         && tcpConnectCommand.getTask().getStatus().equals("SUCCESS")) {
            String mcpFilePath = fileConfig.basePath() + "/model/command" + tcpConnectCommand.getCommandId();
            DronesModel dronesModel = new DronesModel();
            dronesModel.setModelName("雷达扫描模型");
            dronesModel.setModelType("map");
            dronesModel.setFileName("model.zip");
            dronesModel.setFilePath(mcpFilePath + "/" + "model.zip");
            dronesModel.setFileFormat("zip");
            dronesModel.setFileSize(tcpConnectCommand.getTask().getTaskMeta().get("fileLength").asLong());
            dronesModel.setRemarks("无人机上传");
            dronesModelService.register(dronesModel);
            // 将临时文件移动到指定位置
            Path tempFile = Path.of(fileConfig.basePath() + "/temp/received_file.tmp");
            Path targetFile = Path.of(dronesModel.getFilePath());
            try {
                // 确保目录存在
                Files.createDirectories(targetFile.getParent());
                // 移动文件，如果目标文件存在则覆盖
                Files.move(tempFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                log.info("Temp file moved to: " + targetFile);
            } catch (IOException e) {
                log.error("Failed to move temp file to target path", e);
            }
        }
    }

    private void handleError(byte[] body) {
        String json = new String(body, StandardCharsets.UTF_8);
        log.info("ERROR received: " + json);
    }
}
