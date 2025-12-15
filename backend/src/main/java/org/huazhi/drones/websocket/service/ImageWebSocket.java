package org.huazhi.drones.websocket.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;

import io.vertx.core.buffer.Buffer;


@WebSocket(path = "/ws/image")
public class ImageWebSocket {
    private static final Set<WebSocketConnection> sessions = ConcurrentHashMap.newKeySet();

    @OnOpen
    void onOpen(WebSocketConnection conn) {
        sessions.add(conn);
    }

    @OnClose
    void onClose(WebSocketConnection conn) {
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
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println(LocalDateTime.now().format(fmt));
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
