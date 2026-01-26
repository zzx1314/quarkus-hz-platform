package org.huazhi.drones.business.services.entity.dto.yolodto;


public class Yolo {
    private String type = "YOLO";

    private YoloParam param;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public YoloParam getParam() {
        return param;
    }

    public void setParam(YoloParam param) {
        this.param = param;
    }
}
