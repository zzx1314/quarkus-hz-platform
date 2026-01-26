package org.huazhi.drones.ai.controller;

import org.jboss.logging.Logger;


import java.util.Map;

import org.huazhi.drones.ai.assistant.Assistant;
import org.huazhi.drones.ai.tool.CommandTool;

import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/chat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatController {
    private static final Logger log = Logger.getLogger(ChatController.class);
    
    @Inject
    ChatModel openAiChatModel;

    @GET
    @Path("/chatCommands")
    public void chatCommands(@QueryParam("commands") String commands, @QueryParam("deviceId") String deviceId) {
        CommandTool commandTool = new CommandTool();
        Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(openAiChatModel)
        .tools(commandTool)
        .build();

        InvocationParameters parameters = InvocationParameters.from(Map.of("deviceId", deviceId));
        Result<String> result = assistant.chat(commands, parameters);
        log.info("AI Response: " + result.toolExecutions());
    }

}
