package org.hzai.ai.aiknowledgebase.entity.dto;

import lombok.Data;
import jakarta.ws.rs.QueryParam;

@Data
public class AiKnowledgeBaseQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}