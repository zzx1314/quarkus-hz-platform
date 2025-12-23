package org.huazhi.drones.business.services.entity.dto.yolodto;

import lombok.Data;

@Data
public class YoloEvent {
    private String start;

    private String stop;

    private String onDetected;

    private String onLost;
}
