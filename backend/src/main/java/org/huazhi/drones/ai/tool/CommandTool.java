package org.huazhi.drones.ai.tool;

import org.huazhi.drones.ai.command.DroneCommand;
import org.huazhi.drones.ai.command.DroneCommandBatch;

import dev.langchain4j.agent.tool.ReturnBehavior;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandTool {

    @Tool(value = "发送无人机控制指令", returnBehavior = ReturnBehavior.IMMEDIATE)
    public String getCommandString(DroneCommand command) {
        log.info("发送无人机控制指令: {}", command.command);
        log.info("发送无人机控制指令--distance: {}", command.distance);
        log.info("发送无人机控制指令--height: {}", command.height);
        log.info("发送无人机控制指令--duration: {}", command.duration);
        return command.command.toString();
    }

    @Tool(value = "发送一组无人机控制指令，必须按顺序执行", returnBehavior = ReturnBehavior.IMMEDIATE)
    public String sendCommandBatch(DroneCommandBatch batch) {
        for (DroneCommand command : batch.commands) {
            log.info("发送无人机控制指令: {}", command.command);
            log.info("发送无人机控制指令--distance: {}", command.distance);
            log.info("发送无人机控制指令--height: {}", command.height);
            log.info("发送无人机控制指令--duration: {}", command.duration);
        }
        return "已接收指令批次";
    }

}
