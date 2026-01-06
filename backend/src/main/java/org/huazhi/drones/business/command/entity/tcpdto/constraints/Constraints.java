package org.huazhi.drones.business.command.entity.tcpdto.constraints;

import lombok.Data;

@Data
public class Constraints {
    /** TCP 最大存在时间（秒） */
    private long maxDuration;

    /** 最大允许传输字节数 */
    private long maxBytes;

    /** 是否允许断点续传 */
    private boolean allowResume;
}

