package org.hzai.drones.model.entity;

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
public class DronesModel extends PanacheEntityBase{
    @Id
	@GeneratedValue
	private Long id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件格式
     */
    private String fileFormat;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;


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
