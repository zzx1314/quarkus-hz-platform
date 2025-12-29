package org.huazhi.drones.business.task.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
public class DronesTaskQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    /**
     * 任务名称
     */
    @QueryParam("taskName")
    private String taskName;

    /**
     * 任务描述
     */
    @QueryParam("taskDescription")
    private String taskDescription;

    /**
     * 任务状态
     */
    @QueryParam("taskStatus")
    private String taskStatus;


    /**
     * 设备ID
     */
    @QueryParam("deviceIdString")
    private String deviceIdString;

}