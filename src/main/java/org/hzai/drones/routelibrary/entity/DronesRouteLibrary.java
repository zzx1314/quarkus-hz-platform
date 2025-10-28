package org.hzai.drones.routelibrary.entity;

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
@EqualsAndHashCode(callSuper=false)
public class DronesRouteLibrary extends PanacheEntityBase {
    @Id
	@GeneratedValue
	private Long id;

    /**
     * 路线名称
     */
    private String routeName;

    /**
     * 路线描述
     */
    private String routeDescription;

    /**
     * 起点坐标
     */
    private String startCoordinates;

    /**
     * 终点坐标
     */
    private String endCoordinates;

    /**
     * 路线距离（公里）
     */
    private Double distanceKm;

    /**
     * 预计飞行时间（分钟）
     */
    private Integer estimatedFlightTimeMin;

    /**
     * 关联模型的id
     */
    private long modelId;

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
