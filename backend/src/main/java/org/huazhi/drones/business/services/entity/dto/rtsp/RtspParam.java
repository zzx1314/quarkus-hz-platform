package org.huazhi.drones.business.services.entity.dto.rtsp;


public class RtspParam {
    private String url;

    private Long bitrate;

    private RtspEvent event;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getBitrate() {
        return bitrate;
    }

    public void setBitrate(Long bitrate) {
        this.bitrate = bitrate;
    }

    public RtspEvent getEvent() {
        return event;
    }

    public void setEvent(RtspEvent event) {
        this.event = event;
    }
}
