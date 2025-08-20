package org.hzai.ai.aidocument.service.strategy.splitter;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.segment.TextSegment;

import java.util.ArrayList;
import java.util.List;

/**
 * 全文分割器
 */
public class AllWorkSplitter implements DocumentSplitter {
	@Override
	public List<TextSegment> split(Document document) {
		List<TextSegment> textSegments = new ArrayList<>();
		textSegments.add(document.toTextSegment());
		return textSegments;
	}
}
