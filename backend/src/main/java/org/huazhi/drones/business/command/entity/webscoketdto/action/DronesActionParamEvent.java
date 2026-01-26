package org.huazhi.drones.business.command.entity.webscoketdto.action;


public class DronesActionParamEvent {
    private String onComplete;

    private String onFail;

    private String onStart;

    private String onStop;

    private String onTracked;

    private String onLost;

    public String getOnComplete() {
        return onComplete;
    }

    public void setOnComplete(String onComplete) {
        this.onComplete = onComplete;
    }

    public String getOnFail() {
        return onFail;
    }

    public void setOnFail(String onFail) {
        this.onFail = onFail;
    }

    public String getOnStart() {
        return onStart;
    }

    public void setOnStart(String onStart) {
        this.onStart = onStart;
    }

    public String getOnStop() {
        return onStop;
    }

    public void setOnStop(String onStop) {
        this.onStop = onStop;
    }

    public String getOnTracked() {
        return onTracked;
    }

    public void setOnTracked(String onTracked) {
        this.onTracked = onTracked;
    }

    public String getOnLost() {
        return onLost;
    }

    public void setOnLost(String onLost) {
        this.onLost = onLost;
    }

    
}
