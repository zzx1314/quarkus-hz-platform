package org.hzai.ai.chat;

import org.hzai.ai.assistant.WSStreamedAssistant;

import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;

@WebSocket(path = "/ws/stream")
public class WebSocketEndpoint {

    @Inject WSStreamedAssistant assistant;

    @OnTextMessage
    public Multi<String> onTextMessage(String question) {
        return assistant.respondToQuestion(question);
    }
}
