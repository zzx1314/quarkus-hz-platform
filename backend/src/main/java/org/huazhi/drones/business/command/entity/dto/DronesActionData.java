package org.huazhi.drones.business.command.entity.dto;

import java.util.List;

import lombok.Data;

@Data
public class DronesActionData {
    private String event;

    private String action;

    private String height;

    private String speed;

    private List<List<Double>> path;

    private List<String> pathString;

    private Object data;

}
