package org.huazhi.drones.command.entity.webscoketdto.action.takeoff;

import lombok.Data;

@Data
public class DronesActionTakeOffParam {
    private String targetAlt;

    private DronesActionTakeOffParamEvent event;
}
