package org.hzai.ai.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.SessionScoped;

@RegisterAiService
@SystemMessage("You are a helpful AI assistant. Be concise and to the point.")
@SessionScoped
public interface WSStreamedAssistant {

    @UserMessage("Answer the question: {question}")
    Multi<String> respondToQuestion(String question);
}
