package org.huazhi.drones.command.entity.webscoketdto.action;

import lombok.Data;

@Data
public class DronesActionParamEvent {
    private String onComplete;

    private String onFail;

    private String onStart;

    private String onStop;

    private String onTracked;

    private String onLost;
}
