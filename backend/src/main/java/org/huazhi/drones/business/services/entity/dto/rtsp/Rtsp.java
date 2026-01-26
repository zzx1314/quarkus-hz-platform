package org.huazhi.drones.business.services.entity.dto.rtsp;


public class Rtsp {
    private String type = "RTSP";

    private RtspParam param;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RtspParam getParam() {
        return param;
    }

    public void setParam(RtspParam param) {
        this.param = param;
    }
}
