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


    private String nodeType;

    // 任务数据
    private DronesTaskWebScoket taskInfo ;
    // 动作数据
    private DronesAction action;
}
