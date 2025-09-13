package org.hzai.ai.scoringmodel;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/score")
@RegisterRestClient(configKey = "service-score")
public interface HzScoringModeClient {
    @POST
    @Path("/all")
    public List<Double> getScoreAll(ScoringModeDto dto);

    @POST
    public Double getScore(ScoringModeDto dto);
}
