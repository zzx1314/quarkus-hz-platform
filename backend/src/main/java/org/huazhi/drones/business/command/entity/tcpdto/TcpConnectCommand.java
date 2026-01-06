package org.huazhi.drones.business.command.entity.tcpdto;

import java.time.Instant;

import org.huazhi.drones.business.command.entity.tcpdto.constraints.Constraints;
import org.huazhi.drones.business.command.entity.tcpdto.enumdata.CommandType;
import org.huazhi.drones.business.command.entity.tcpdto.session.Session;
import org.huazhi.drones.business.command.entity.tcpdto.targetdata.Target;
import org.huazhi.drones.business.command.entity.tcpdto.task.Task;

import lombok.Data;

@Data
public class TcpConnectCommand {

    /** 指令唯一标识（幂等 / 审计） */
    private String commandId;

    /** 固定为 TCP_CONNECT */
    private CommandType commandType;

    /** 指令下发时间 */
    private Instant issuedAt;

    /** 指令过期时间 */
    private Instant expiresAt;

    /** TCP 会话信息（核心） */
    private Session session;

    /** TCP 目标地址 */
    private Target target;

    /** 本次 TCP 要执行的任务 */
    private Task task;

    /** 约束条件 */
    private Constraints constraints;
}
