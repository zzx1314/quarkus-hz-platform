package org.hzai.ai.aidocument.service.strategy.splitter;

import org.hzai.ai.aidocument.service.strategy.DocumentSplittingStrategy;

import dev.langchain4j.data.document.DocumentSplitter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WordHeadingSplittingStrategy implements DocumentSplittingStrategy {
	@Override
	public DocumentSplitter getSplitter() {
		return new WordHeadingSplitter();
	}
}
