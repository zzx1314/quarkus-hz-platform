package org.huazhi.ai.aiproducer;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class StreamModelProducer {

    @Produces
    @ApplicationScoped
    public StreamingChatModel embeddingModel() {
        OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder()
        .apiKey("11")
        .modelName("gpt-4o-mini")
        .build();
        return model;
    }
}
