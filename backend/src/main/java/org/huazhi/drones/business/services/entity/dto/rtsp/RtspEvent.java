package org.huazhi.drones.business.services.entity.dto.rtsp;


public class RtspEvent {
    private String start;

    private String stop;

    private String onComplete;

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

    public String getOnComplete() {
        return onComplete;
    }

    public void setOnComplete(String onComplete) {
        this.onComplete = onComplete;
    }
}
