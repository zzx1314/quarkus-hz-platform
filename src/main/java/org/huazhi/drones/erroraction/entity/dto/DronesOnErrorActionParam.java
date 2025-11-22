package org.huazhi.drones.erroraction.entity.dto;

import lombok.Data;

@Data
public class DronesOnErrorActionParam {
    private String reason;

    private DronesOnErrorActionEvent event;
}
