package org.huazhi.drones.command.entity.webscoketdto.action.service;

import java.util.List;

import lombok.Data;

@Data
public class DronesActionServiceParam {
    private List<String> type;

    private DronesActionServiceParamEvent event;
}
