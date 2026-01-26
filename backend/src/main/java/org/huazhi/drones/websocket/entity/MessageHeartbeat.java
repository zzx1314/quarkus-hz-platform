package org.huazhi.drones.websocket.entity;


public class MessageHeartbeat {
    private String deviceId;

    private String type;

    private String status;

    private MessageDrones drones;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MessageDrones getDrones() {
        return drones;
    }

    public void setDrones(MessageDrones drones) {
        this.drones = drones;
    }

}
