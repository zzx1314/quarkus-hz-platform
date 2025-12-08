package org.huazhi.drones.commanditem.entity;

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
public class DronesCommandResultItem extends PanacheEntityBase{
    @Id
    @GeneratedValue
    private Long id;

    /*
     * 指令ID
     */
    private Long commandId;


    /*
     * 指令结果
     */
    @Column(columnDefinition = "TEXT")
    private String commandResult;

     /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updateTime;

     /**
     * 是否删除
     */
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer isDeleted;

}
