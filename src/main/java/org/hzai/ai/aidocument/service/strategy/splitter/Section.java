package org.hzai.ai.aidocument.service.strategy.splitter;

public class Section {
	private String heading;
	private StringBuilder content;

	public Section() {
	}

	public Section(String heading) {
		this.heading = heading;
		this.content = new StringBuilder();
	}

	public String getHeading() {
		return heading;
	}

	public String getContent() {
		return content.toString().trim();
	}

	public void appendContent(String text) {
		if (text != null && !text.trim().isEmpty()) {
			if (content.length() > 0) content.append("\n");
			content.append(text.trim());
		}
	}
}
