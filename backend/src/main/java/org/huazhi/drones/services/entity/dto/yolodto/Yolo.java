package org.huazhi.drones.services.entity.dto.yolodto;

import lombok.Data;

@Data
public class Yolo {
    private String type = "YOLO";

    private YoloParam param;
}
