package org.huazhi.drones.business.workflow.entity;

import java.util.List;


/**
 * 平行的集合
 */
public class DeviceNodeActionParallelGroup {
    private String type;

    private List<String> list;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
