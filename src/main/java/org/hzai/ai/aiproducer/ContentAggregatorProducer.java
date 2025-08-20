package org.hzai.ai.aiproducer;

import dev.langchain4j.model.scoring.onnx.OnnxScoringModel;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.aggregator.ReRankingContentAggregator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ContentAggregatorProducer {
    @Produces
    @ApplicationScoped
    public ContentAggregator contentAggregator() {
        // 评分模型
        String pathToModel = "/home/zzx/IdeaProjects/bge-reranker-base/onnx/model.onnx";
        String pathToTokenizer = "/home/zzx/IdeaProjects/bge-reranker-base/tokenizer.json";
        OnnxScoringModel scoringModel = new OnnxScoringModel(pathToModel, pathToTokenizer);

        return ReRankingContentAggregator.builder()
                .scoringModel(scoringModel)
                .minScore(0.8)
                .build();
    }
}
