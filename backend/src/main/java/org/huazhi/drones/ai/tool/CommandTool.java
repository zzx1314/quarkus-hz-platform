package org.huazhi.drones.ai.tool;

import java.util.ArrayList;
import java.util.List;

import org.huazhi.drones.ai.command.DroneCommand;
import org.huazhi.drones.ai.command.DroneCommandBatch;
import org.huazhi.drones.business.command.entity.webscoketdto.DronesCommandWebsocket;
import org.huazhi.drones.business.command.entity.webscoketdto.action.DronesAction;
import org.huazhi.drones.business.command.entity.webscoketdto.action.DronesActionParam;
import org.huazhi.drones.business.command.entity.webscoketdto.action.DronesActionParamEvent;
import org.huazhi.drones.business.command.entity.webscoketdto.task.DronesTaskWebScoket;
import org.huazhi.drones.websocket.service.ConnectionManager;
import org.huazhi.util.IdUtil;

import dev.langchain4j.agent.tool.ReturnBehavior;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class CommandTool {

    @Inject
    ConnectionManager connectionManager;

    @Tool(value = "发送无人机控制指令", returnBehavior = ReturnBehavior.IMMEDIATE)
    public String getCommandString(DroneCommand command) {
        log.info("发送无人机控制指令: {}", command.command);
        log.info("发送无人机控制指令--distance: {}", command.distance);
        log.info("发送无人机控制指令--height: {}", command.height);
        log.info("发送无人机控制指令--duration: {}", command.duration);
        return command.command.toString();
    }

    @Tool(value = "发送一组无人机控制指令，必须按顺序执行", returnBehavior = ReturnBehavior.IMMEDIATE)
    public String sendCommandBatch(DroneCommandBatch batch) {
        DronesCommandWebsocket commandWebsocket = new DronesCommandWebsocket();
        commandWebsocket.setType("toros");
        List<DronesTaskWebScoket> tasks = new ArrayList<>();
        DronesTaskWebScoket task = new DronesTaskWebScoket();
        task.setTaskId("ai_task_001");
        task.setEvent("init_task");
        
        List<DronesAction> actions = new ArrayList<>();


        String beforeId = "init_task";
        for (int i = 0; i < batch.commands.size(); i++) {
            String currentId = "action_" + IdUtil.simpleUUID();
            DroneCommand cmd = batch.commands.get(i);
            switch (cmd.command) {
                case TAKEOFF -> {
                    log.info("起飞指令: {}", cmd.command);
                    DronesAction action = new DronesAction();
                    action.setActionId("action_takeoff_001");
                    action.setType("TAKEOFF");
                    action.setAfter(beforeId);
                    action.setTimeoutSec(20);
                    DronesActionParam param = new DronesActionParam();
                    param.setTargetAlt(0.5);
                    DronesActionParamEvent event = new DronesActionParamEvent();
                    event.setOnComplete(currentId);
                    event.setOnFail("node_lfql6sga");
                    param.setEvent(event);
                    actions.add(action);
                }
                case HOVER -> {
                    log.info("悬停指令: {}", cmd.command);
                }
                case LAND -> {
                    log.info("降落指令: {}", cmd.command);
                }
                case MOVE_FORWARD -> {
                    log.info("前进指令: {}，距离: {}", cmd.command, cmd.distance);
                }
                case MOVE_BACKWARD -> {
                    log.info("后退指令: {}，距离: {}", cmd.command, cmd.distance);
                }
                case MOVE_LEFT -> {
                    log.info("左移指令: {}，距离: {}", cmd.command, cmd.distance);
                }
                case MOVE_RIGHT -> {
                    log.info("右移指令: {}，距离: {}", cmd.command, cmd.distance);
                }
                case ASCEND -> {
                    log.info("上升指令: {}，高度: {}", cmd.command, cmd.height);
                }   
                case START_RTSP -> {
                    log.info("启动 RTSP 服务");
                }
                case START_YOLO -> {
                    log.info("启动 YOLO 服务");
                }
                default -> log.warn("未知指令: {}", cmd.command);
            }

            beforeId = currentId;
        }

        task.setActions(actions);
        tasks.add(task);
        return "已接收指令批次";
    }

}
