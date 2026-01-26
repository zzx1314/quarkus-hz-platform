package org.huazhi.drones.business.services.entity.dto.deepsort;


public class DeepsortEvent {
    private String start;

    private String stop;

    private String onTracked;

    private String onLost;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
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
