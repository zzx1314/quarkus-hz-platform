package org.huazhi.drones.websocket.endpoint;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.logging.Logger;

import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;

import io.vertx.core.buffer.Buffer;

@WebSocket(path = "/ws/image")
public class ImageWebSocket {
     private static final Logger log = Logger.getLogger(ImageWebSocket.class);
    private static final Set<WebSocketConnection> sessions = ConcurrentHashMap.newKeySet();

    @OnOpen
    void onOpen(WebSocketConnection conn) {
        log.info("开启image---webscoket连接");
        sessions.add(conn);
    }

    @OnClose
    void onClose(WebSocketConnection conn) {
        log.info("关闭image---webscoket连接");
        sessions.remove(conn);
    }

    public static void broadcast(String base64) {
        for (WebSocketConnection conn : sessions) {
            if (conn.isOpen()) {
                conn.sendText(base64)
                .subscribe().with(
                    v -> {},
                    err -> err.printStackTrace()
                );
            }
        }
    }

     public static void broadcast(byte[] jpeg) {
        for (WebSocketConnection conn : sessions) {
            if (!conn.isOpen()) continue;

            conn.sendBinary(Buffer.buffer(jpeg))
                .subscribe().with(
                    v -> {},
                    Throwable::printStackTrace
                );
        }
    }
}
