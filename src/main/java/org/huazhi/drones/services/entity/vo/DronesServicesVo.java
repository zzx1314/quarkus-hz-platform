package org.huazhi.drones.services.entity.vo;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class DronesServicesVo {
    /**
     * 类型
     */
    private String type;

    /**
     * 参数
     */
    private JsonNode params;
}
