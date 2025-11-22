package org.huazhi.drones.command.entity.webscoketdto.action.takeoff;

import lombok.Data;

@Data
public class DronesActionTakeOff {
    private String actionId;
    
    private String type = "TAKEOFF";

    private DronesActionTakeOffParam param;

    /**
     * 接受节点
     */
    private String after;

    private Integer timeoutSec;

}
