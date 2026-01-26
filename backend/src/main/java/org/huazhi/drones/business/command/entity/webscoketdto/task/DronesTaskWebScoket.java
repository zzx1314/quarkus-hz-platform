package org.huazhi.drones.business.command.entity.webscoketdto.task;

import java.util.List;

import org.huazhi.drones.business.command.entity.webscoketdto.action.DronesAction;
import org.huazhi.drones.business.command.entity.webscoketdto.route.DronesRoute;


public class DronesTaskWebScoket {
    private String taskId;

    private DronesRoute fromWp;

    private DronesRoute toWp;

    private String event;

    private List<DronesAction> actions;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public DronesRoute getFromWp() {
        return fromWp;
    }

    public void setFromWp(DronesRoute fromWp) {
        this.fromWp = fromWp;
    }

    public DronesRoute getToWp() {
        return toWp;
    }

    public void setToWp(DronesRoute toWp) {
        this.toWp = toWp;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public List<DronesAction> getActions() {
        return actions;
    }

    public void setActions(List<DronesAction> actions) {
        this.actions = actions;
    }
}
