package org.hzai.ai.aiparagraph.entity.dto;

import lombok.Data;
import jakarta.ws.rs.QueryParam;

@Data
public class AiParagraphQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}