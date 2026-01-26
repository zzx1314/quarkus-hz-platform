package org.huazhi.drones.business.workflow.entity;

import org.huazhi.drones.business.command.entity.webscoketdto.action.DronesAction;
import org.huazhi.drones.business.command.entity.webscoketdto.task.DronesTaskWebScoket;


/**
 * 节点数据
 */
public class NodeEntityData {
    private String taskId;

    private Long routeId;

    private Long deviceId;

    // 节点类型
    private String nodeType;

    // 具体动作类型
    private String type;

    // 任务数据
    private DronesTaskWebScoket taskInfo;
    // 动作数据
    private DronesAction action;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    // 节点类型
    public String getNodeType() {
        return nodeType;
    }

    // 节点类型
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    // 具体动作类型
    public String getType() {
        return type;
    }

    // 具体动作类型
    public void setType(String type) {
        this.type = type;
    }

    // 任务数据
    public DronesTaskWebScoket getTaskInfo() {
        return taskInfo;
    }

    // 任务数据
    public void setTaskInfo(DronesTaskWebScoket taskInfo) {
        this.taskInfo = taskInfo;
    }

    // 动作数据
    public DronesAction getAction() {
        return action;
    }

    // 动作数据
    public void setAction(DronesAction action) {
        this.action = action;
    }
}
