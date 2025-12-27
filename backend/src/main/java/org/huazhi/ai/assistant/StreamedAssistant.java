package org.huazhi.ai.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.smallrye.mutiny.Multi;

@SystemMessage("你是一位乐于助人的AI助手。请简明扼要地说明要点。")
public interface StreamedAssistant {

    @UserMessage("回答下面的问题: {question}")
    Multi<String> respondToQuestion(String question);
}