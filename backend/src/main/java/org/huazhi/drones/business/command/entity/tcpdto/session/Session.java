package org.huazhi.drones.business.command.entity.tcpdto.session;

import lombok.Data;

@Data
public class Session {

    /** 服务端生成的 TCP 会话 ID */
    private String sessionId;

    /** 一次性 token，用于 TCP AUTH */
    private String token;

    /** token 有效期（秒） */
    private long ttl;

}
