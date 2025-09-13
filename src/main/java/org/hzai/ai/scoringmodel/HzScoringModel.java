package org.hzai.ai.scoringmodel;


import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.scoring.ScoringModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class HzScoringModel implements ScoringModel {
    @Inject
    @RestClient
    HzScoringModeClient scoringModeClient;

    @Override
    public Response<Double> score(String text, String query) {
        return ScoringModel.super.score(text, query);
    }

    @Override
    public Response<Double> score(TextSegment segment, String query) {
        return ScoringModel.super.score(segment, query);
    }

    @Override
    public Response<List<Double>> scoreAll(List<TextSegment> list, String query) {
        ScoringModeDto dto = new ScoringModeDto();
        dto.setQuestion(query);
        dto.setTextSegments(list);
        List<Double> scores = scoringModeClient.getScoreAll(dto);
        return Response.from(scores);
    }
}
