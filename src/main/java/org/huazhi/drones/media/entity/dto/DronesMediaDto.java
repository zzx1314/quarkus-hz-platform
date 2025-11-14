package org.huazhi.drones.media.entity.dto;

import lombok.Data;

@Data
public class DronesMediaDto {
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
     * 媒体文件描述
     */
    private String remarks;
}