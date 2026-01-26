package org.huazhi.drones.business.command.entity.dto;

import java.util.List;

public class DronesActionData {
    private String event;

    private String action;

    private String height;

    private String speed;

    private List<List<Double>> path;

    private List<String> pathString;

    private Object data;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public List<List<Double>> getPath() {
        return path;
    }

    public void setPath(List<List<Double>> path) {
        this.path = path;
    }

    public List<String> getPathString() {
        return pathString;
    }

    public void setPathString(List<String> pathString) {
        this.pathString = pathString;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    
}
