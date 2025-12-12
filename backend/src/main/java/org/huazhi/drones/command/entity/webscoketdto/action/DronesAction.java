package org.huazhi.drones.command.entity.webscoketdto.action;

import lombok.Data;

@Data
public class DronesAction {
    private String actionId;

    private String type;

    private String after;

    private Integer timeoutSec;

    private DronesActionParam params;
}
