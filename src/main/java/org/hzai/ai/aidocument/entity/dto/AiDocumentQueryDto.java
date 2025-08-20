package org.hzai.ai.aidocument.entity.dto;

import lombok.Data;
import jakarta.ws.rs.QueryParam;

@Data
public class AiDocumentQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    @QueryParam("knowledgeId")
    private Long knowledgeId;

    @QueryParam("fileName")
    private String fileName;
}