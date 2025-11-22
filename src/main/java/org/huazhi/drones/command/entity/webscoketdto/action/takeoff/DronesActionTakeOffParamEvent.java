package org.huazhi.drones.command.entity.webscoketdto.action.takeoff;

import lombok.Data;

@Data
public class DronesActionTakeOffParamEvent {
    private String onComplete;

    private String onFail;
}
