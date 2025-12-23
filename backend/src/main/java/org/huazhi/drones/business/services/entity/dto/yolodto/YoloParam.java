package org.huazhi.drones.business.services.entity.dto.yolodto;

import lombok.Data;

@Data
public class YoloParam {
    private String model;

    private String input;

    private YoloEvent event;

}
