package org.huazhi.ai;

import org.junit.jupiter.api.Test;

import dev.langchain4j.model.openai.OpenAiChatModel;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AiModelTest {
    @Test
    public void testChat() {
        OpenAiChatModel model = OpenAiChatModel.builder()
        .baseUrl("https://api.chatanywhere.tech/v1")
        .apiKey("sk-CNIeY38hqWm8YGobx4kpT8Hey9RA1AYziTrVfWZLqZwhP9Xz")
        .modelName("gpt-5-mini")
        .logRequests(true)
        .logResponses(true)
        .build();
        String answer = model.chat("Say 'Hello World'");
        System.out.println(answer); // Hello World
    }
}
