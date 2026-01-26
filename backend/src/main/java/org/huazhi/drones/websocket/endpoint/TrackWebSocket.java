package org.huazhi.drones.websocket.endpoint;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.logging.Logger;

import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.vertx.core.buffer.Buffer;

@WebSocket(path = "/ws/track")
public class TrackWebSocket {
     private static final Logger log = Logger.getLogger(TrackWebSocket.class);
    private static final Set<WebSocketConnection> sessions = ConcurrentHashMap.newKeySet();

    @OnOpen
    void onOpen(WebSocketConnection conn) {
        log.info("开启track---webscoket连接");
        sessions.add(conn);
    }

    @OnClose
    void onClose(WebSocketConnection conn) {
        log.info("关闭track---webscoket连接");
        sessions.remove(conn);
    }

    public static void broadcast(String base64) {
        for (WebSocketConnection conn : sessions) {
            if (conn.isOpen()) {
                conn.sendText(base64)
                        .subscribe().with(
                                v -> {
                                },
                                err -> err.printStackTrace());
            }
        }
    }

    public static void broadcast(byte[] jpeg, String tag) {
        byte[] tagBytes = tag != null ? tag.getBytes(StandardCharsets.UTF_8) : new byte[0];

        ByteBuffer buffer = ByteBuffer.allocate(1 + 4 + tagBytes.length + jpeg.length);
        buffer.put((byte) 0x01);      // 协议标识
        buffer.putInt(tagBytes.length); // 字符串长度
        buffer.put(tagBytes); // 字符串
        buffer.put(jpeg); // 二进制数据
        buffer.flip();

        for (WebSocketConnection conn : sessions) {
            if (!conn.isOpen())
                continue;

            conn.sendBinary(Buffer.buffer(buffer.array()))
                    .subscribe().with(
                            v -> {
                            },
                            Throwable::printStackTrace);
        }
    }
}
