package org.huazhi.drones.ai.model;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@Startup
@ApplicationScoped
public class ChatModelProducer {
    @Inject
    ChatModelConfig config;

    @Produces
    @ApplicationScoped
    ChatModel openAiChatModel() {
        return OllamaChatModel.builder()
                .baseUrl(config.baseUrl())
                .modelName(config.modelName())
                .think(false)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
