package org.huazhi.drones.business.command.entity.tcpdto;


import org.huazhi.drones.business.command.entity.tcpdto.targetdata.Target;
import org.huazhi.drones.business.command.entity.tcpdto.task.Task;

import lombok.Data;

@Data
public class TcpConnectCommand {

    /** 指令唯一标识（幂等 / 审计） */
    private Long commandId;

    /** TCP 目标地址 */
    private Target target;

    /** 本次 TCP 要执行的任务 */
    private Task task;
}
