package org.huazhi.drones.command.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 无人机指令
 * </p>
 *
 * @author zzx
 * @since 2024-06-10
 */
@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
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
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer isDeleted;

    /**
     * 任务ID
     */
    private Long taskId;

}
