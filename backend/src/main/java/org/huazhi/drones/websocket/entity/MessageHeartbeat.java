package org.huazhi.drones.websocket.entity;

import lombok.Data;

@Data
public class MessageHeartbeat {
    private String deviceId;

    private String type;

    private String status;

    private MessageDrones drones;

}
