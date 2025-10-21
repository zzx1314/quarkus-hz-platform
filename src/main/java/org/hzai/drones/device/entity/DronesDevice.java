package org.hzai.drones.device.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 无人机设备
 * </p>
 *
 * @author zzx
 * @since 2024-06-10
 */
@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class DronesDevice extends PanacheEntityBase {
    @Id
	@GeneratedValue
	private Long id;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备序列号
     */
    private String serialNumber;

    /**
     * 设备状态
     */
    private String status;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 设备位置
     */
    private String location;

    /**
     * 设备制造商
     */
    private String manufacturer;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 固件版本
     */
    private String firmwareVersion;

    /**
     * 电池状态
     */
    private String batteryStatus;

    /**
     * 最后维护日期
     */
    private String lastMaintenanceDate;


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
     private Integer isDeleted;
}
