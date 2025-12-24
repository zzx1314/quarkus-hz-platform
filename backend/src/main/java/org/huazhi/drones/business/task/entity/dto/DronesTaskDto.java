package org.huazhi.drones.business.task.entity.dto;

import lombok.Data;

@Data
public class DronesTaskDto {
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 设备Id
     */
    private Long deviceId;

    /**
     * 任务描述
     */
    private String taskDescription;

    /**
     * 任务状态
     */
    private String taskStatus;

}