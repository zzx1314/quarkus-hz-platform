package org.huazhi.drones.services.entity.dto.yolodto;

import lombok.Data;

@Data
public class YoloEvent {
    private String start;

    private String stop;

    private String onDetected;

    private String onLost;
}
