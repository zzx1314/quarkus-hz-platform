package org.huazhi.drones.command.entity.webscoketdto;

import java.util.List;

import org.huazhi.drones.command.entity.webscoketdto.action.DronesAction;
import org.huazhi.drones.command.entity.webscoketdto.task.DronesTaskWebScoket;
import org.huazhi.drones.services.entity.vo.DronesServicesVo;

import lombok.Data;

@Data
public class DronesCommandWebsocket {

    /*
    * 设备id
    */
    private String deviceId;
    /**
     * 类型
     */
    private String type;
    /**
     * 指令id
     */
    private String missionId;

    /**
     * 路线
     */
    private List<DronesRoute> route;

    /**
     * 服务列表
     */
    private List<DronesServicesVo> services;

    /**
     * 错误动作列表
     */
    private List<DronesAction> onErrorActions;

    /**
     * 任务列表
     */
    private List<DronesTaskWebScoket> tasks;
}
