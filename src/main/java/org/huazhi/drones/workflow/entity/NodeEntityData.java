package org.huazhi.drones.workflow.entity;

import org.huazhi.drones.command.entity.webscoketdto.DronesTaskWebScoket;
import org.huazhi.drones.command.entity.webscoketdto.action.gotow.DronesActionGoto;
import org.huazhi.drones.command.entity.webscoketdto.action.hover.DronesActionHover;
import org.huazhi.drones.command.entity.webscoketdto.action.phone.DronesActionPhoto;
import org.huazhi.drones.command.entity.webscoketdto.action.service.DronesActionService;
import org.huazhi.drones.command.entity.webscoketdto.action.takeoff.DronesActionTakeOff;

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
    private DronesActionGoto gotoAction;
    
    private DronesActionHover hoverAction;

    private DronesActionPhoto photoAction;

    private DronesActionService startService;

    private DronesActionService stopService;

    private DronesActionTakeOff takeOffAction;    

}
