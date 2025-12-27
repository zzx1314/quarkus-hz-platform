package org.huazhi.ai.aidocument.service.strategy;

import org.huazhi.ai.aidocument.service.strategy.splitter.AllWorkSplitter;

import dev.langchain4j.data.document.DocumentSplitter;

public class AllWorkSplitterStrategy implements DocumentSplittingStrategy {
	@Override
	public DocumentSplitter getSplitter() {
		return new AllWorkSplitter();
	}
}
