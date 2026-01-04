package org.huazhi.drones.business.task.entity.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DronesTaskVo {
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 设备ID
     */
    private Long deviceId;


    /**
     * 设备ID字符串
     */
    private String deviceIdString;

    /**
     * 任务描述
     */
    private String taskDescription;

    /**
     * 任务状态
     */
    private String taskStatus;

    /*
     * 工作流UUID
     */
    private String workflowUuid;

    /**
     * 路线ID
     */
    private Long routeId;


    /**
     * 航线名称
     */
    private String routeName;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;

}
