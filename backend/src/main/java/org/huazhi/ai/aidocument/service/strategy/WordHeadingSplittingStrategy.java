package org.huazhi.ai.aidocument.service.strategy;

import org.huazhi.ai.aidocument.service.strategy.splitter.WordHeadingSplitter;

import dev.langchain4j.data.document.DocumentSplitter;


public class WordHeadingSplittingStrategy implements DocumentSplittingStrategy {
	@Override
	public DocumentSplitter getSplitter() {
		return new WordHeadingSplitter();
	}
}
