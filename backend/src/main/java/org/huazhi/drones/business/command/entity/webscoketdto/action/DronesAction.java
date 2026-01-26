package org.huazhi.drones.business.command.entity.webscoketdto.action;


public class DronesAction {
    private String actionId;

    private String type;

    private String after;

    private Integer timeoutSec;

    private DronesActionParam params;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public Integer getTimeoutSec() {
        return timeoutSec;
    }

    public void setTimeoutSec(Integer timeoutSec) {
        this.timeoutSec = timeoutSec;
    }

    public DronesActionParam getParams() {
        return params;
    }

    public void setParams(DronesActionParam params) {
        this.params = params;
    }
}
