package org.huazhi.drones.business.command.entity.dto;

import lombok.Data;

@Data
public class DronesActionTrigger {
    private String triggerId;

    private String triggerType;

    private Object triggerData;

    private Precondition precondition;
}
