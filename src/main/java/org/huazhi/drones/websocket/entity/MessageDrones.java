package org.huazhi.drones.websocket.entity;

import lombok.Data;

@Data
public class MessageDrones {
    private String speed;

    private String height;

    private String battery;

    private String course;

    private String location;
}
