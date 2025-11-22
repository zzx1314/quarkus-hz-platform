package org.huazhi.drones.command.entity.webscoketdto.action.hover;

import lombok.Data;

@Data
public class DronesActionHover {
    private String actionId;
    private String type = "HOVER";

    private DronesActionHoverParam param;

    private String after;

    private Integer timeoutSec;
}
