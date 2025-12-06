package org.huazhi.drones.workflow.entity;

import org.huazhi.drones.command.entity.webscoketdto.action.DronesAction;
import org.huazhi.drones.command.entity.webscoketdto.task.DronesTaskWebScoket;

import lombok.Data;

/**
 * 节点数据
 */
@Data
public class NodeEntityData {
    private Integer taskId;

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
}
