package org.huazhi.drones.business.workflow.entity;

import lombok.Data;

/**
 * 设备节点-起飞动作
 */
@Data
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
}
