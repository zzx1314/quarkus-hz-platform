package org.huazhi.drones.task.entity.dto;

import lombok.Data;

@Data
public class DronesTaskDto {
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务描述
     */
    private String taskDescription;

}