package org.huazhi.drones.ai.tool;

import java.util.ArrayList;
import java.util.List;

import org.huazhi.drones.ai.command.DroneCommand;
import org.huazhi.drones.ai.command.DroneCommandBatch;
import org.huazhi.drones.business.command.entity.webscoketdto.DronesCommandWebsocket;
import org.huazhi.drones.business.command.entity.webscoketdto.action.DronesAction;
import org.huazhi.drones.business.command.entity.webscoketdto.action.DronesActionParam;
import org.huazhi.drones.business.command.entity.webscoketdto.action.DronesActionParamEvent;
import org.huazhi.drones.business.command.entity.webscoketdto.route.DronesRoute;
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
                    action.setTimeoutSec(100);

                    DronesActionParam param = new DronesActionParam();
                    param.setTargetAlt(0.5);
                    DronesActionParamEvent event = new DronesActionParamEvent();
                    event.setOnComplete(currentId);
                    event.setOnFail("node_lfql6sga");
                    param.setEvent(event);

                    action.setParams(param);
                    actions.add(action);
                }
                case HOVER -> {
                    log.info("悬停指令: {}", cmd.command);
                    DronesAction action = new DronesAction();
                    action.setActionId("action_hover_001");
                    action.setType("HOVER");
                    action.setAfter(beforeId);
                    action.setTimeoutSec(100);

                    DronesActionParam param = new DronesActionParam();
                    param.setTimeSec(cmd.duration);
                    DronesActionParamEvent event = new DronesActionParamEvent();
                    event.setOnComplete(currentId);
                    event.setOnFail("node_lfql6sga");
                    param.setEvent(event);

                    action.setParams(param);
                    actions.add(action);
                }
                case LAND -> {
                    log.info("降落指令: {}", cmd.command);
                    DronesAction action = new DronesAction();
                    action.setActionId("action_land_001");
                    action.setType("LAND");
                    action.setAfter(beforeId);
                    action.setTimeoutSec(100);

                    DronesActionParam param = new DronesActionParam();
                    DronesActionParamEvent event = new DronesActionParamEvent();
                    event.setOnComplete(currentId);
                    event.setOnFail("node_lfql6sga");
                    param.setEvent(event);

                    action.setParams(param);
                    actions.add(action);
                }
                case MOVE_FORWARD -> {
                    log.info("前进指令: {}，距离: {}", cmd.command, cmd.distance);
                    DronesAction action = new DronesAction();
                    action.setActionId("action_move_forward_001");
                    action.setType("GOTO");
                    action.setAfter(beforeId);
                    action.setTimeoutSec(100);

                    DronesActionParam param = new DronesActionParam();
                    DronesRoute targetWp = new DronesRoute();
                    targetWp.setWpId(1);
                    targetWp.setLat(cmd.distance);
                    targetWp.setLon(0.0);
                    targetWp.setAlt(0.5);
                    param.setTargetWp(targetWp);

                    DronesActionParamEvent event = new DronesActionParamEvent();
                    event.setOnComplete(currentId);
                    event.setOnFail("node_lfql6sga");
                    param.setEvent(event);
                }
                case MOVE_BACKWARD -> {
                    log.info("后退指令: {}，距离: {}", cmd.command, cmd.distance);
                    DronesAction action = new DronesAction();
                    action.setActionId("action_move_backward_001");
                    action.setType("GOTO");
                    action.setAfter(beforeId);
                    action.setTimeoutSec(100);

                    DronesActionParam param = new DronesActionParam();
                    DronesRoute targetWp = new DronesRoute();
                    targetWp.setWpId(1);
                    targetWp.setLat(-cmd.distance);
                    targetWp.setLon(0.0);
                    targetWp.setAlt(0.5);
                    param.setTargetWp(targetWp);

                    DronesActionParamEvent event = new DronesActionParamEvent();
                    event.setOnComplete(currentId);
                    event.setOnFail("node_lfql6sga");
                    param.setEvent(event);
                }
                case MOVE_LEFT -> {
                    log.info("左移指令: {}，距离: {}", cmd.command, cmd.distance);
                    DronesAction action = new DronesAction();
                    action.setActionId("action_move_left_001");
                    action.setType("GOTO");
                    action.setAfter(beforeId);
                    action.setTimeoutSec(100);

                    DronesActionParam param = new DronesActionParam();
                    DronesRoute targetWp = new DronesRoute();
                    targetWp.setWpId(1);
                    targetWp.setLat(0.0);
                    targetWp.setLon(cmd.distance);
                    targetWp.setAlt(0.5);
                    param.setTargetWp(targetWp);

                    DronesActionParamEvent event = new DronesActionParamEvent();
                    event.setOnComplete(currentId);
                    event.setOnFail("node_lfql6sga");
                    param.setEvent(event);
                }
                case MOVE_RIGHT -> {
                    log.info("右移指令: {}，距离: {}", cmd.command, cmd.distance);
                    DronesAction action = new DronesAction();
                    action.setActionId("action_move_right_001");
                    action.setType("GOTO");
                    action.setAfter(beforeId);
                    action.setTimeoutSec(100);

                    DronesActionParam param = new DronesActionParam();
                    DronesRoute targetWp = new DronesRoute();
                    targetWp.setWpId(1);
                    targetWp.setLat(0.0);
                    targetWp.setLon(-cmd.distance);
                    targetWp.setAlt(0.5);
                    param.setTargetWp(targetWp);

                    DronesActionParamEvent event = new DronesActionParamEvent();
                    event.setOnComplete(currentId);
                    event.setOnFail("node_lfql6sga");
                    param.setEvent(event);
                }
                case ASCEND -> {
                    log.info("上升指令: {}，高度: {}", cmd.command, cmd.height);
                }   
                case START_RTSP -> {
                    log.info("启动 RTSP 服务");
                    DronesAction action = new DronesAction();
                    action.setActionId("action_rtsp_001");
                    action.setType("SERVICE_START_RTSP");
                    action.setAfter(beforeId);
                    action.setTimeoutSec(100);

                    DronesActionParam param = new DronesActionParam();
                    param.setType("RTSP");

                    DronesActionParamEvent event = new DronesActionParamEvent();
                    event.setOnComplete(currentId);
                    event.setOnFail("node_lfql6sga");
                    event.setOnStart("rtsp_start");
                    param.setEvent(event);
                }
                case START_YOLO -> {
                    log.info("启动 YOLO 服务");
                    DronesAction action = new DronesAction();
                    action.setActionId("action_yolo_001");
                    action.setType("SERVICE_START_YOLO");
                    action.setAfter(beforeId);
                    action.setTimeoutSec(100);

                    DronesActionParam param = new DronesActionParam();
                    param.setType("YOLO");

                    DronesActionParamEvent event = new DronesActionParamEvent();
                    event.setOnComplete(currentId);
                    event.setOnFail("node_lfql6sga");
                    event.setOnStart("yolo_start");
                    param.setEvent(event);
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
