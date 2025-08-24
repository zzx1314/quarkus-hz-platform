package org.hzai.ai.aidocument.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
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