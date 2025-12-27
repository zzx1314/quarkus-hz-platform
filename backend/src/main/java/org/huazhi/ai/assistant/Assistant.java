package org.huazhi.ai.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;


public interface Assistant {
	@SystemMessage("你是一个人工助手")
	TokenStream chat(String message);

	@SystemMessage("你是一个人工助手")
    TokenStream chatForEachUse(@MemoryId Long memoryId, @UserMessage String userMessage);

	@UserMessage("你是一个人工助手，{{message}}")
	String chatProcess(@V("message") String userMessage);

	@UserMessage("你是一个人工助手，请将下面的语句整理成简洁通顺的语句进行回复。{{message}}")
	String chatProcessMcp(@V("message") String userMessage);
}
