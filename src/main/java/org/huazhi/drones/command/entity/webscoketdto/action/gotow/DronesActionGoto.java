package org.huazhi.drones.command.entity.webscoketdto.action.gotow;

import lombok.Data;

@Data
public class DronesActionGoto {
    private String actionId;

    private String type = "GOTO";

    private DronesActionGotoParam param;

    private String after;

    private Integer timeoutSec;
}
