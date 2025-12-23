package org.huazhi.drones.business.services.entity.dto.yolodto;

import lombok.Data;

@Data
public class Yolo {
    private String type = "YOLO";

    private YoloParam param;
}
