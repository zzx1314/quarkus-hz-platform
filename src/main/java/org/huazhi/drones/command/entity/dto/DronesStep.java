package org.huazhi.drones.command.entity.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 无人机执行步骤
 */
@Data
public class DronesStep {
    /**
     * 任务编号
     */
    private String taskNumber;

    /**
     * 任务目标
     */
    private Map<String, String> taskTarget;


    /**
     * 任务路径
     */
    private List<List<Double>> routePath;

}
