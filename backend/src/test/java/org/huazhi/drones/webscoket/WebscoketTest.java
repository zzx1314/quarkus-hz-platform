package org.huazhi.drones.webscoket;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketClient;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import static org.awaitility.Awaitility.await;

@QuarkusTest
public class WebscoketTest {

    @Inject
    Vertx vertx;

    @Test
    public void testOnBinaryMessage() throws Exception {

        String deviceId = "0020007010500009";
        String accessToken = "b96041e798b64a1790946c2222cf6539";

        WebSocketClient client = vertx.createWebSocketClient();

        WebSocket ws = client
                .connect(
                        8081,
                        "localhost",
                        "/api/notice/" + deviceId + "/" + accessToken
                )
                .toCompletionStage()
                .toCompletableFuture()
                .join();
        byte[] imageBytes = Files.readAllBytes(Path.of("src/test/resources/model.png"));


        ws.writeBinaryMessage(Buffer.buffer(imageBytes));

        await()
        .atMost(Duration.ofSeconds(3))
        .untilAsserted(() -> {
            assertTrue(
                Files.list(Path.of("/data/image"))
                    .anyMatch(p -> p.toString().endsWith(".png"))
            );
        });

        ws.close();
        client.close();
    }
}
