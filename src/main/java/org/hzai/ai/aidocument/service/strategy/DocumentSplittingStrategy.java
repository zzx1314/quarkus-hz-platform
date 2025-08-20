package org.hzai.ai.aidocument.service.strategy;

import dev.langchain4j.data.document.DocumentSplitter;

public interface DocumentSplittingStrategy {
	DocumentSplitter getSplitter();
}
