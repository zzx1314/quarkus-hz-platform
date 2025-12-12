package org.huazhi.drones.command.entity.dto;

import lombok.Data;

@Data
public class DronesAction {
    private String actionId;

    private String actionType;

    private DronesActionData actionData;

    private String timeout;

    private String endTask;
}
