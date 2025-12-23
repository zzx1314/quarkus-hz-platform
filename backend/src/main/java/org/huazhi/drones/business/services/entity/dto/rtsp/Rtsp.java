package org.huazhi.drones.business.services.entity.dto.rtsp;

import lombok.Data;

@Data
public class Rtsp {
    private String type = "RTSP";

    private RtspParam param;
}
