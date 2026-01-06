package org.huazhi.drones.business.command.entity.tcpdto.targetdata;

import lombok.Data;

@Data
public class Target {

    /** TCP 服务端地址 */
    private String host;

    /** TCP 端口 */
    private int port;

}

