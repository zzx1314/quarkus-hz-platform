package org.huazhi.drones.business.workflow.entity;

import java.util.List;

import org.huazhi.drones.business.command.entity.dto.DronesAction;


/**
 * 任务节点
 */
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
     * 并行的动作
     */
    private DeviceNodeActionParallelGroup parallelGroup;

    /**
     * 超时
     */
    private String timeout;

    /**
     * 视频采集--1
     */
    public DronesAction getVideoCapture() {
        return videoCapture;
    }

    /**
     * 视频采集--1
     */
    public void setVideoCapture(DronesAction videoCapture) {
        this.videoCapture = videoCapture;
    }

    /**
     * 拍照--2
     */
    public DronesAction getTakePhoto() {
        return takePhoto;
    }

    /**
     * 拍照--2
     */
    public void setTakePhoto(DronesAction takePhoto) {
        this.takePhoto = takePhoto;
    }

    /**
     * 目标识别--3
     */
    public DronesAction getTargetRecognition() {
        return targetRecognition;
    }

    /**
     * 目标识别--3
     */
    public void setTargetRecognition(DronesAction targetRecognition) {
        this.targetRecognition = targetRecognition;
    }

    /**
     * 移动--4
     */
    public DronesAction getMoveBase() {
        return moveBase;
    }

    /**
     * 移动--4
     */
    public void setMoveBase(DronesAction moveBase) {
        this.moveBase = moveBase;
    }

    /**
     * 顺序动作
     */
    public List<String> getSequence() {
        return sequence;
    }

    /**
     * 顺序动作
     */
    public void setSequence(List<String> sequence) {
        this.sequence = sequence;
    }

    /**
     * 并行的动作
     */
    public DeviceNodeActionParallelGroup getParallelGroup() {
        return parallelGroup;
    }

    /**
     * 并行的动作
     */
    public void setParallelGroup(DeviceNodeActionParallelGroup parallelGroup) {
        this.parallelGroup = parallelGroup;
    }

    /**
     * 超时
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * 超时
     */
    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

}
