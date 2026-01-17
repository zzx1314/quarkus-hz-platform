package org.huazhi.ai;

import org.huazhi.drones.ai.assistant.Assistant;
import org.huazhi.drones.ai.tool.CommandTool;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AiModelTest {

    @Inject
    ChatModel openAiChatModel;

    @Test
    @Order(1)
    public void testModel() {
        CommandTool commandTool = new CommandTool();
        Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(openAiChatModel)
        .tools(commandTool)
        .build();
        Result<String> result = assistant.chat("先起飞，向前飞行5米，悬停1分钟");
        System.out.println("AI Response: " + result.toolExecutions());
    }
}

