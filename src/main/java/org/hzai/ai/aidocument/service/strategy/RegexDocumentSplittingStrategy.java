package org.hzai.ai.aidocument.service.strategy;

import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentByCharacterSplitter;
import dev.langchain4j.data.document.splitter.DocumentByRegexSplitter;

public class RegexDocumentSplittingStrategy implements DocumentSplittingStrategy {

	@Override
	public DocumentSplitter getSplitter() {
		return new DocumentByRegexSplitter(
				"\\n\\d+(\\.\\d+)?\\、",
				"\n",
				100,
				20,
				new DocumentByCharacterSplitter(500, 50)
		);
	}
}
