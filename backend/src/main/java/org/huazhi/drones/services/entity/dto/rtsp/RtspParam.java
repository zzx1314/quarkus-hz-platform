package org.huazhi.drones.services.entity.dto.rtsp;

import lombok.Data;

@Data
public class RtspParam {
    private String url;

    private Long bitrate;

    private RtspEvent event;
}
