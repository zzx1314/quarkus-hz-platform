package org.hzai.drones.model.entity.dto;

import lombok.Data;

@Data
public class DronesModelDto {
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
}