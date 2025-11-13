package org.hzai.drones.config.entity.dto;

import lombok.Data;

@Data
public class DronesConfigDto {
    private Long id;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 描述
     */
    private String description;

}