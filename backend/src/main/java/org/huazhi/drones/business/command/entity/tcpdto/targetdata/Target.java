package org.huazhi.drones.business.command.entity.tcpdto.targetdata;

import io.netty.handler.codec.haproxy.HAProxyProxiedProtocol.TransportProtocol;
import lombok.Data;

@Data
public class Target {

    /** TCP 服务端地址 */
    private String host;

    /** TCP 端口 */
    private int port;

    /** tcp / tls / quic（预留） */
    private TransportProtocol protocol;

}

