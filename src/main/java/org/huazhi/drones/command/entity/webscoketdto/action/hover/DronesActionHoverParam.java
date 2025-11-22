package org.huazhi.drones.command.entity.webscoketdto.action.hover;

import lombok.Data;

@Data
public class DronesActionHoverParam {
    private Integer timeSec;

    private DronesActionHoverParamEvent event;
}
