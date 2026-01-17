package org.huazhi.drones.ai.command;

import java.util.List;

import lombok.Data;

@Data
public class DroneCommandBatch {
     public List<DroneCommand> commands;
}
