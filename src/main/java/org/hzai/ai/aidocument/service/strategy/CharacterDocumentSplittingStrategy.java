package org.hzai.ai.aidocument.service.strategy;


import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentByCharacterSplitter;

public class CharacterDocumentSplittingStrategy implements DocumentSplittingStrategy {

	private final int segmentSize;
	private final int overlapSize;

	public CharacterDocumentSplittingStrategy(Integer segmentSize, Integer overlapSize) {
		this.segmentSize = segmentSize;
		this.overlapSize = overlapSize;
	}

	@Override
	public DocumentSplitter getSplitter() {
		return new DocumentByCharacterSplitter(segmentSize, overlapSize);
	}
}

