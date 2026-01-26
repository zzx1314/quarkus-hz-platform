package org.huazhi.drones.business.workflow.entity;


/**
 * 设备节点-起飞动作
 */
public class DeviceNodeTakeoff {
    /**
     * 起飞动作
     */
    private String action;

    /**
     * 起飞高度
     */
    private String height;

    /**
     * 起飞速度
     */
    private String speed;

    /**
     * 起飞时间
     */
    private String timeout;

    /**
     * 起飞动作
     */
    public String getAction() {
        return action;
    }

    /**
     * 起飞动作
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * 起飞高度
     */
    public String getHeight() {
        return height;
    }

    /**
     * 起飞高度
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * 起飞速度
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * 起飞速度
     */
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    /**
     * 起飞时间
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * 起飞时间
     */
    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
