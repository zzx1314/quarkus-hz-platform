package org.huazhi.ai.aidocument.service.strategy;

import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentByRegexSplitter;

public class FlagDocumentSplittingStrategy implements DocumentSplittingStrategy {
	private final String flag;

	public FlagDocumentSplittingStrategy(String flag) {
		this.flag = flag;
	}

	@Override
	public DocumentSplitter getSplitter() {
		String regex = "";
		if ("换行".equals(flag)) {
			regex = "\\n+";
		} else if ("分号".equals(flag)) {
			regex = "；";
		} else if ("句号".equals(flag)) {
			regex = "。";
		} else if ("冒号".equals(flag)) {
			regex = "：";
		}
		return new DocumentByRegexSplitter(
				regex,
				"\n",
				500,
				50
		);
	}
}
