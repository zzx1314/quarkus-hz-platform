package org.hzai.ai.aiproducer;

import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.aggregator.ReRankingContentAggregator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import org.hzai.ai.scoringmodel.HzScoringModel;

@ApplicationScoped
public class ContentAggregatorProducer {
    @Inject
    HzScoringModel hzScoringModel;
    @Produces
    @ApplicationScoped
    public ContentAggregator contentAggregator() {
        // 评分模型
        return ReRankingContentAggregator.builder()
                .scoringModel(hzScoringModel)
                .minScore(0.8)
                .build();
    }
}
