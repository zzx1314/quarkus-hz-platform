package org.huazhi.ai.aiparagraph.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
public class AiParagraphQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("docId")
    private Long docId;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}