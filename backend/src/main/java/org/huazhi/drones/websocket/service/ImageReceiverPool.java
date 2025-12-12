package org.huazhi.drones.websocket.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@Slf4j
public class ImageReceiverPool {

    // key = host:port
    private final Map<String, ImageReceiver> receivers = new ConcurrentHashMap<>();

    public synchronized void start(String host, int port) {

        String key = host + ":" + port;

        if (receivers.containsKey(key)) {
            log.warn("[ReceiverPool] Receiver already running: {}", key);
            return;
        }

        ImageReceiver receiver = new ImageReceiver(host, port);
        receiver.start();

        receivers.put(key, receiver);
        log.info("[ReceiverPool] Started receiver {}", key);
    }

    public synchronized void stop(String host, int port) {
        String key = host + ":" + port;

        ImageReceiver receiver = receivers.remove(key);
        if (receiver == null) {
            log.warn("[ReceiverPool] Receiver not found: {}", key);
            return;
        }

        receiver.stop();
        log.info("[ReceiverPool] Stopped receiver {}", key);
    }

    public synchronized void stopAll() {
        receivers.values().forEach(ImageReceiver::stop);
        receivers.clear();
        log.info("[ReceiverPool] Stopped ALL receivers");
    }
}
