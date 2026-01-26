package org.huazhi.drones.business.services.entity.dto.yolodto;


public class YoloParam {
    private String model;

    private String input;

    private YoloEvent event;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public YoloEvent getEvent() {
        return event;
    }

    public void setEvent(YoloEvent event) {
        this.event = event;
    }

}
