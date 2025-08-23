package org.hzai.ai.aimodel.entity.dto;

import lombok.Data;
import jakarta.ws.rs.QueryParam;

@Data
public class AiModelQueryDto {
    @QueryParam("id")
    private Long id;

    private String enable;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}