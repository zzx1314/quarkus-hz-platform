package org.hzai.ai.aiprocess.entity.dto;

import lombok.Data;
import jakarta.ws.rs.QueryParam;

@Data
public class AiProcessQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}