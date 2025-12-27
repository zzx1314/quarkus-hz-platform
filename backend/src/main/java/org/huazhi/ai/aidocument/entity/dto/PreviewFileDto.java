package org.huazhi.ai.aidocument.entity.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Data;

@Data
public class PreviewFileDto {
	@QueryParam("fileName")
	private String fileName;

	@QueryParam("strategy")
	private String strategy;

	@QueryParam("flag")
	private String flag;

	@QueryParam("length")
	private String length;

	@QueryParam("path")
	private String path;
}
