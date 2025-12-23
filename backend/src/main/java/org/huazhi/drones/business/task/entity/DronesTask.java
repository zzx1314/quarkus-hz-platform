package org.huazhi.drones.business.task.entity;

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

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DronesTask extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务描述
     */
    private String taskDescription;

    /**
     * 任务状态
     */
    private String taskStatus;

    /**
     * 工作流ID
     */
    private Long workflowId;

    /*
     * 工作流UUID
     */
    private String workflowUuid;

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
}
