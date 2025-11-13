package org.hzai.drones.command.entity.dto;

import java.util.List;
import java.util.Map;


import lombok.Data;

@Data
public class DronesCommandWebsocket {
    Long id;
    
    String deviceId;

    String type;
    /**
     * 配置信息
     */
    Map<String, String> config;

    /**
     * 模型信息
     */
    Map<String, String> model;

    /**
     * 路线信息
     */
    List<LocationInfo> route;
 }
