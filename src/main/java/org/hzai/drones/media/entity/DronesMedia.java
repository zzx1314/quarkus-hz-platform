package org.hzai.drones.media.entity;

import java.time.LocalDateTime;

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
public class DronesMedia extends PanacheEntityBase {
    @Id
	@GeneratedValue
	private Long id;

    /**
     * 媒体文件名称
     */
    private String mediaName;

    /**
     * 媒体文件类型
     */
    private String mediaType;

    /**
     * 媒体文件路径
     */
    private String mediaPath;

    /**
     * 媒体文件大小（字节）
     */
    private Long mediaSize;

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
