package org.hzai.ai.aidocument.service.strategy.splitter;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import lombok.SneakyThrows;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WordHeadingSplitter implements DocumentSplitter {
	@SneakyThrows
	@Override
	public List<TextSegment> split(Document document) {
		Metadata metadata = document.metadata();
		String absoluteDirectoryPath = metadata.getString("absolute_directory_path");
		String fileName = metadata.getString("file_name");
		File file = new File(absoluteDirectoryPath + File.separator + fileName);
		List<Section> sections = new ArrayList<>();
		Section currentSection = null;
		try (InputStream inputStream = new FileInputStream(file);
			 XWPFDocument doc = new XWPFDocument(inputStream)) {

			// 遍历文档的所有body元素（段落或表格）
			List<IBodyElement> bodyElements = doc.getBodyElements();
			String currentHeading = null;
			for (IBodyElement element : bodyElements) {
				if (element instanceof XWPFParagraph) {
					XWPFParagraph para = (XWPFParagraph) element;
					String style = para.getStyle();
					String text = para.getText().trim();
					if (text.isEmpty()) continue;

					if (style != null && (style.startsWith("Heading") || style.matches("[1-6]"))) {
						currentHeading = text;
						// 新建一个Section对象
						currentSection = new Section(text);
						sections.add(currentSection);

					} else if (currentHeading != null && currentSection != null) {
						currentSection.appendContent(text);
					}
				} else if (element instanceof XWPFTable) {
					// 可选：表格内容拼成字符串，加入当前Section
					XWPFTable table = (XWPFTable) element;
					StringBuilder tableText = new StringBuilder("[表格]\n");
					for (XWPFTableRow row : table.getRows()) {
						for (XWPFTableCell cell : row.getTableCells()) {
							tableText.append(cell.getText().trim()).append(" | ");
						}
						tableText.append("\n");
					}
					if (currentSection != null) {
						currentSection.appendContent(tableText.toString());
					}
				}
			}

			List<TextSegment> segments = new ArrayList<>();

			for (Section sec : sections) {
				if (!sec.getContent().isEmpty()) {
					TextSegment segment = TextSegment.from(sec.getContent(), Metadata.from("HEADING", sec.getHeading()));
					System.out.println("标题: " + sec.getHeading());
					System.out.println("内容:\n" + sec.getContent());
					System.out.println("----------");
					segments.add(segment);
				}
			}
			return segments;
		}
	}

}
