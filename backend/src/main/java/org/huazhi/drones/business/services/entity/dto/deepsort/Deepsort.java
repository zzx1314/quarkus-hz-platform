package org.huazhi.drones.business.services.entity.dto.deepsort;


public class Deepsort {
    private String type = "DEEPSORT";

    private DeepsortParam param;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DeepsortParam getParam() {
        return param;
    }

    public void setParam(DeepsortParam param) {
        this.param = param;
    }

}
