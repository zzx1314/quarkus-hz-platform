package org.huazhi.ai.aiproducer;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallzhq.BgeSmallZhQuantizedEmbeddingModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class EmbeddingModelProducer {
    @Produces
    @ApplicationScoped
    public EmbeddingModel embeddingModel() {
        return new BgeSmallZhQuantizedEmbeddingModel();
    }
}
