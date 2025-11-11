package org.hzai.drones.websocket.entity;

import org.jose4j.json.internal.json_simple.JSONObject;

import lombok.Data;

@Data
public class MessageInfo {
    private Long id;

    /**
     * 设备ID
     */
    private String deviceId;

     /**
     * 速度
     */
    private String speed;

    /**
     * 高度
     */
    private String height;

    /**
     * 剩余电量
     */
    private String battery;

    /**
     * 航向
     */
    private String course;

    /**
     * 位置
     */
    private String location;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息内容
     */
    private Object msg;

    /**
     * 下发指令的参数
     */
    private JSONObject data;

    /**
     * 执行结果
     */
    private String status;

    /**
     * 执行结果返回值
     */
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
