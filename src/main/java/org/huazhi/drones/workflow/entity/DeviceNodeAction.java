package org.huazhi.drones.workflow.entity;

import java.util.List;

import org.huazhi.drones.command.entity.dto.DronesAction;

import lombok.Data;

/**
 * 任务节点
 */
@Data
public class DeviceNodeAction {
    /**
     * 视频采集--1
     */
    private DronesAction videoCapture;

    /**
     * 拍照--2
     */
    private DronesAction takePhoto;

    /**
     * 目标识别--3
     */
    private DronesAction targetRecognition;

    /**
     * 移动--4
     */
    private DronesAction moveBase;

    /**
     * 顺序动作
     */
    private List<String> sequence;


    /**
     * 超时
     */
    private String timeout;

    
}
