package org.huazhi.drones.ai.command;

import java.util.Map;

import org.huazhi.drones.ai.tool.DroneCommandType;

import lombok.Data;

@Data
public class DroneCommand {

    public DroneCommandType command;

    // 可选参数
    public Double distance;
    public Double height;
    public Integer duration;


    public Map<String, Object> params;
}
