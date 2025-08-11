package org.hzai.ai.chat;

import org.hzai.ai.assistant.StreamedAssistant;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/stream")
public class AiChat {
     @Inject 
     StreamedAssistant assistant;

    @POST
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> stream(String question) {
        return assistant.respondToQuestion(question);
    }
}
