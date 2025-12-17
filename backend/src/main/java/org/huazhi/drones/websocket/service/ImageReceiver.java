package org.huazhi.drones.websocket.service;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.huazhi.drones.websocket.endpoint.ImageWebSocket;

@Slf4j
public class ImageReceiver implements Runnable {

    private final String host;
    private final int port;
    private volatile boolean running = false;
    private Socket socket;

    public ImageReceiver(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        if (running) return;
        running = true;
        Thread t = new Thread(this, "receiver-" + host + "-" + port);
        t.start();
    }

    public void stop() {
        running = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();  // 中断阻塞的 read()
            }
        } catch (Exception ignore) {}
    }

    @Override
    public void run() {
        while (running) {
            try (Socket s = new Socket()) {

                this.socket = s;
                s.connect(new InetSocketAddress(host, port), 5000);

                log.info("[ImageReceiver] Connected to {}:{}", host, port);

                InputStream in = s.getInputStream();
                byte[] header = new byte[4];

                while (running) {
                    readFully(in, header);
                    int imageSize = ByteBuffer.wrap(header)
                            .order(ByteOrder.LITTLE_ENDIAN)
                            .getInt();

                    byte[] block = new byte[imageSize];
                    readFully(in, block);

                    int channelId = ByteBuffer.wrap(block, 0, 4)
                            .order(ByteOrder.LITTLE_ENDIAN)
                            .getInt();

                    if (channelId != 0) continue;

                    byte[] jpeg = new byte[imageSize - 4];
                    System.arraycopy(block, 4, jpeg, 0, jpeg.length);

                    ImageWebSocket.broadcast(jpeg);
                }

            } catch (Exception e) {
                if (running) {
                    log.error("[ImageReceiver] {}:{} error: {}", host, port, e.getMessage());
                }
                try { Thread.sleep(2000); } catch (Exception ignore) {}
            }
        }

        log.info("[ImageReceiver] Stopped for {}:{}", host, port);
    }

    private void readFully(InputStream in, byte[] buffer) throws Exception {
        int n = 0;
        while (n < buffer.length) {
            int c = in.read(buffer, n, buffer.length - n);
            if (c < 0) throw new RuntimeException("Disconnected");
            n += c;
        }
    }
}
