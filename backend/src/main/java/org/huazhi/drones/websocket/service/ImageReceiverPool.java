package org.huazhi.drones.websocket.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.logging.Logger;

@ApplicationScoped
public class ImageReceiverPool {
     private static final Logger log = Logger.getLogger(ImageReceiverPool.class);

    // key = host:port
    private final Map<String, ImageReceiver> receivers = new ConcurrentHashMap<>();

    public synchronized void start(String host, int port) {

        String key = host + ":" + port;

        if (receivers.containsKey(key)) {
            log.warnf("[ReceiverPool] Receiver already running: %s", key);
            return;
        }

        ImageReceiver receiver = new ImageReceiver(host, port);
        receiver.start();

        receivers.put(key, receiver);
        log.infof("[ReceiverPool] Started receiver %s", key);
    }

    public synchronized void stop(String host, int port) {
        String key = host + ":" + port;

        ImageReceiver receiver = receivers.remove(key);
        if (receiver == null) {
            log.warnf("[ReceiverPool] Receiver not found: %s", key);
            return;
        }

        receiver.stop();
        log.infof("[ReceiverPool] Stopped receiver %s", key);
    }

    public synchronized void stopAll() {
        receivers.values().forEach(ImageReceiver::stop);
        receivers.clear();
        log.info("[ReceiverPool] Stopped ALL receivers");
    }
}
