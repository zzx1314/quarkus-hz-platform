package org.huazhi.drones.command.entity.webscoketdto.action.service;

import lombok.Data;

/*
服务开启和关闭
 */
@Data
public class DronesActionService {
    private String actionId;

    private String type;

    private DronesActionServiceParam params;

    private String after;
}
