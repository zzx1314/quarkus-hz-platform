package org.hzai.drones.command.entity;

import java.time.LocalDateTime;

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
@EqualsAndHashCode(callSuper=false)
public class DronesCommand extends PanacheEntityBase {
    @Id
	@GeneratedValue
	private Long id;

    /**
     * 指令名称
     */
    private String commandName;

    /**
     * 指令参数
     */
    private String commandParams;

    /**
     * 目标设备ID
     */
    private Long deviceId;

    /**
     * 指令状态
     */
    private String status;

    /**
     * 返回值
     */
    private String returnValue;

     /**
     * 创建时间
     */
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
