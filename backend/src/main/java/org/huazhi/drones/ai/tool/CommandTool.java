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
import org.huazhi.util.JsonUtil;

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
        log.info("distance: {}", command.distance);
        log.info("height: {}", command.height);
        log.info("duration: {}", command.duration);
        return command.command.toString();
    }

    @Tool(value = "发送一组无人机控制指令，必须按顺序执行", returnBehavior = ReturnBehavior.IMMEDIATE)
    public String sendCommandBatch(DroneCommandBatch batch) {
        DronesCommandWebsocket websocket = new DronesCommandWebsocket();
        websocket.setType("toros");

        DronesTaskWebScoket task = new DronesTaskWebScoket();
        task.setTaskId("ai_task_" + IdUtil.simpleUUID());
        task.setEvent("init_task");

        List<DronesAction> actions = new ArrayList<>();
        String beforeId = "init_task";

        for (DroneCommand cmd : batch.commands) {
            DronesAction action = buildAction(cmd, beforeId);
            if (action != null) {
                actions.add(action);
                beforeId = action.getActionId();
            }
        }

        task.setActions(actions);
        websocket.setTasks(List.of(task));

        connectionManager.sendMessageByDeviceId(websocket.getDeviceId(), JsonUtil.toJson(websocket));
        return "已接收指令批次";
    }

    // =========================
    // Action Builder Dispatcher
    // =========================

    private DronesAction buildAction(DroneCommand cmd, String afterId) {

        String actionId = "action_" + IdUtil.simpleUUID();

        return switch (cmd.command) {
            case TAKEOFF -> buildTakeoff(actionId, afterId);
            case HOVER -> buildHover(actionId, afterId, cmd.duration);
            case LAND -> buildLand(actionId, afterId);
            case MOVE_FORWARD -> buildMove(actionId, afterId, cmd.distance, 0);
            case MOVE_BACKWARD -> buildMove(actionId, afterId, -cmd.distance, 0);
            case MOVE_LEFT -> buildMove(actionId, afterId, 0, cmd.distance);
            case MOVE_RIGHT -> buildMove(actionId, afterId, 0, -cmd.distance);
            case START_RTSP -> buildService(actionId, afterId, "SERVICE_START_RTSP", "RTSP");
            case START_YOLO -> buildService(actionId, afterId, "SERVICE_START_YOLO", "YOLO");
            default -> {
                log.warn("未知指令: {}", cmd.command);
                yield null;
            }
        };
    }

    // =========================
    // Common Builders
    // =========================

    private DronesAction baseAction(String actionId, String type, String afterId) {
        DronesAction action = new DronesAction();
        action.setActionId(actionId);
        action.setType(type);
        action.setAfter(afterId);
        action.setTimeoutSec(100);
        return action;
    }

    private DronesActionParamEvent buildEvent(String onComplete) {
        DronesActionParamEvent event = new DronesActionParamEvent();
        event.setOnComplete(onComplete);
        event.setOnFail("node_fail");
        return event;
    }

    // =========================
    // Specific Action Builders
    // =========================

    private DronesAction buildTakeoff(String id, String after) {
        log.info("起飞指令");

        DronesAction action = baseAction(id, "TAKEOFF", after);

        DronesActionParam param = new DronesActionParam();
        param.setTargetAlt(0.5);
        param.setEvent(buildEvent(id));

        action.setParams(param);
        return action;
    }

    private DronesAction buildHover(String id, String after, Integer duration) {
        log.info("悬停指令，duration={}", duration);

        DronesAction action = baseAction(id, "HOVER", after);

        DronesActionParam param = new DronesActionParam();
        param.setTimeSec(duration);
        param.setEvent(buildEvent(id));

        action.setParams(param);
        return action;
    }

    private DronesAction buildLand(String id, String after) {
        log.info("降落指令");

        DronesAction action = baseAction(id, "LAND", after);

        DronesActionParam param = new DronesActionParam();
        param.setEvent(buildEvent(id));

        action.setParams(param);
        return action;
    }

    private DronesAction buildMove(String id, String after, double lat, double lon) {
        log.info("移动指令 lat={}, lon={}", lat, lon);

        DronesAction action = baseAction(id, "GOTO", after);

        DronesRoute wp = new DronesRoute();
        wp.setWpId(1);
        wp.setLat(lat);
        wp.setLon(lon);
        wp.setAlt(0.5);

        DronesActionParam param = new DronesActionParam();
        param.setTargetWp(wp);
        param.setEvent(buildEvent(id));

        action.setParams(param);
        return action;
    }

    private DronesAction buildService(String id, String after, String type, String serviceType) {
        log.info("启动服务: {}", serviceType);

        DronesAction action = baseAction(id, type, after);

        DronesActionParam param = new DronesActionParam();
        param.setType(serviceType);

        DronesActionParamEvent event = buildEvent(id);
        event.setOnStart(serviceType.toLowerCase() + "_start");

        param.setEvent(event);
        action.setParams(param);
        return action;
    }
}
