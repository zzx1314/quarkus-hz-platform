package org.huazhi.drones.command.entity.webscoketdto.action.gotow;

import org.huazhi.drones.command.entity.webscoketdto.DronesRoute;

import lombok.Data;

@Data
public class DronesActionGotoParam {
    private String targetWpId;

    private DronesRoute targetWp;

    private DronesActionGotoParamEvent event;
}
