package org.huazhi.drones.workflow.entity;

import org.huazhi.drones.command.entity.webscoketdto.DronesTaskWebScoket;
import org.huazhi.drones.command.entity.webscoketdto.action.DronesAction;

import lombok.Data;

/**
 * 节点数据
 */
@Data
public class NodeEntityData {
    private Integer taskId;

    private Long routeId;

    private Long deviceId;


    private String actionType;

    // 任务数据
    private DronesTaskWebScoket taskInfo ;
    // 动作数据
    private DronesAction action;
}
