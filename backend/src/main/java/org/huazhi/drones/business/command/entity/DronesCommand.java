package org.huazhi.drones.business.command.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;

/**
 * <p>
 * 无人机指令
 * </p>
 *
 * @author zzx
 * @since 2024-06-10
 */
@Entity
public class DronesCommand extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 指令名称
     */
    private String commandName;

    /**
     * 指令类型
     */
    private String commandType;

    /**
     * 指令参数
     */
    @Column(columnDefinition = "TEXT")
    private String commandParams;

    /**
     * 目标设备ID
     */
    private String deviceId;

    /**
     * 指令状态
     */
    private String status;

    /**
     * 返回值
     */
    @Column(columnDefinition = "TEXT")
    private String returnValue;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @Column(columnDefinition = "INT DEFAULT 0",  insertable = false)
    private Integer isDeleted;

    /**
     * 任务ID
     */
    private Long taskId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getCommandParams() {
        return commandParams;
    }

    public void setCommandParams(String commandParams) {
        this.commandParams = commandParams;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    

}
