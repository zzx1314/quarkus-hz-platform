package org.huazhi.drones.business.services.entity.dto.yolodto;


public class YoloEvent {
    private String start;

    private String stop;

    private String onDetected;

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

    public String getOnDetected() {
        return onDetected;
    }

    public void setOnDetected(String onDetected) {
        this.onDetected = onDetected;
    }

    public String getOnLost() {
        return onLost;
    }

    public void setOnLost(String onLost) {
        this.onLost = onLost;
    }
}
