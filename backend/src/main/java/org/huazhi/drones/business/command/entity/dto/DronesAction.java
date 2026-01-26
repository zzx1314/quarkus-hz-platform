package org.huazhi.drones.business.command.entity.dto;

public class DronesAction {
    private String actionId;

    private String actionType;

    private DronesActionData actionData;

    private String timeout;

    private String endTask;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public DronesActionData getActionData() {
        return actionData;
    }

    public void setActionData(DronesActionData actionData) {
        this.actionData = actionData;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getEndTask() {
        return endTask;
    }

    public void setEndTask(String endTask) {
        this.endTask = endTask;
    }

    
}
