package org.huazhi.drones.command.entity.webscoketdto;

import java.util.List;

import org.huazhi.drones.erroraction.entity.DronesOnErrorAction;

import lombok.Data;

@Data
public class DronesCommandWebsocketV1 {
    /**
     * 类型
     */
    private String type;
    /**
     * 指令id
     */
    private Long missionId;

    /**
     * 路线
     */
    private List<DronesRoute> route;

    /**
     * 服务列表
     */
    private List<Object> services; 


    /**
     * 错误动作列表
     */
    private List<DronesOnErrorAction> onErrorActions;

    /**
     * 任务列表
     */
    private List<DronesTaskWebScoket> tasks;
}
