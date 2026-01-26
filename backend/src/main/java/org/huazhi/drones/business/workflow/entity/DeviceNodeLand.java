package org.huazhi.drones.business.workflow.entity;


/**
 * 降落节点
 */
public class DeviceNodeLand {
    private String action;

    private String timeout;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
