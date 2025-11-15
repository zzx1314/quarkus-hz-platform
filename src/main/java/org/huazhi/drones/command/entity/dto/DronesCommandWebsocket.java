package org.huazhi.drones.command.entity.dto;

import java.util.List;

import lombok.Data;

@Data
public class DronesCommandWebsocket {
    Long id;
    
    /**
     * 设备id
     */
    String deviceId;

    /**
     * 命令类型
     */
    String type;

    /**
     * 步骤
     */
    List<DronesStep> stepArray;
 }
