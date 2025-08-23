package org.hzai.ai.assistant;


import org.hzai.ai.aiapplication.entity.IntentRecognition;

import dev.langchain4j.service.UserMessage;

public interface AssistantAnaly {
    @UserMessage("判断下面用户输入的语义意图文。{{it}}")
	IntentRecognition analyzeIntention(String text);
}
