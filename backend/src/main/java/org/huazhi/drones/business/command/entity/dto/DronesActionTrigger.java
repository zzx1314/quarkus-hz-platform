package org.huazhi.drones.business.command.entity.dto;


public class DronesActionTrigger {
    private String triggerId;

    private String triggerType;

    private Object triggerData;

    private Precondition precondition;

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public Object getTriggerData() {
        return triggerData;
    }

    public void setTriggerData(Object triggerData) {
        this.triggerData = triggerData;
    }

    public Precondition getPrecondition() {
        return precondition;
    }

    public void setPrecondition(Precondition precondition) {
        this.precondition = precondition;
    }
}
