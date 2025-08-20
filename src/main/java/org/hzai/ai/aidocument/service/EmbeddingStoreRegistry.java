package org.hzai.ai.aidocument.service;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import dev.langchain4j.store.embedding.EmbeddingStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * EmbeddingStore 注册中心，支持按知识库 ID 获取对应 EmbeddingStore
 */
@ApplicationScoped
public class EmbeddingStoreRegistry {
	@Inject
	private EmbeddingModel embeddingModel;

	private final Map<Long, EmbeddingStore<?>> stores = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
    public <T> EmbeddingStore<T> getStore(Long knowledgeBaseId) {
        EmbeddingStore<T> store = (EmbeddingStore<T>) stores.get(knowledgeBaseId);
        if (store == null) {
			store = (EmbeddingStore<T>) PgVectorEmbeddingStore.builder()
					.host("localhost")
					.port(5432)
					.database("knowledge")
					.user("huzhi")
					.password("123456")
					.table("embedding_store_"+ knowledgeBaseId)
					.dimension(embeddingModel.dimension())
					.build();
			stores.put(knowledgeBaseId, store);
        }
        return store;
    }

	public <T> void registerStore(Long knowledgeBaseId, EmbeddingStore<T> store) {
		stores.put(knowledgeBaseId, store);
	}

	public boolean containsStore(Long knowledgeBaseId) {
		return stores.containsKey(knowledgeBaseId);
	}

	public void removeStore(Long knowledgeBaseId) {
		stores.remove(knowledgeBaseId);
	}
}
