package org.huazhi.drones.command.entity.webscoketdto.action.gotow;

import lombok.Data;

@Data
public class DronesActionGotoParamEvent {
    private String onComplete;

    private String onFail;
}
