package org.hzai.ai.aidocument.service.strategy;

import org.hzai.ai.aidocument.service.strategy.splitter.WordHeadingSplitter;

import dev.langchain4j.data.document.DocumentSplitter;


public class WordHeadingSplittingStrategy implements DocumentSplittingStrategy {
	@Override
	public DocumentSplitter getSplitter() {
		return new WordHeadingSplitter();
	}
}
