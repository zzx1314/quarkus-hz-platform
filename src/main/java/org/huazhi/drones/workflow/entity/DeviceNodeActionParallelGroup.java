package org.huazhi.drones.workflow.entity;

import java.util.List;

import lombok.Data;

/**
 * 平行的集合
 */
@Data
public class DeviceNodeActionParallelGroup {
    private String type;

    private List<String> list;
}
