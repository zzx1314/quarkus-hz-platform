package org.huazhi.drones.command.entity.webscoketdto;

import java.util.List;


import lombok.Data;

@Data
public class DronesTaskWebScoket {
    private String taskId;

    private DronesRoute fromWp;

    private DronesRoute toWp;

    private String event;

    private List<Object> actions;
}
