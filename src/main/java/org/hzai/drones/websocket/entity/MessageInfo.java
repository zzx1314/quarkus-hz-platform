package org.hzai.drones.websocket.entity;

import org.jose4j.json.internal.json_simple.JSONObject;

import lombok.Data;

@Data
public class MessageInfo {
    private Long id;

    private String deviceId;

    private String type;

    private Object msg;

    private JSONObject data;

    private String status;

    private String returnValue;

    public MessageInfo() {
    }

    public MessageInfo(String type, Object msg) {
        this.type = type;
        this.msg = msg;
    }

    public MessageInfo(String type, Object msg, JSONObject data) {
        this.type = type;
        this.msg = msg;
        this.data = data;
    }
}
