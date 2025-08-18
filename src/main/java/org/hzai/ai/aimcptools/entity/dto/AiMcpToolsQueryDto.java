package org.hzai.ai.aimcptools.entity.dto;

import lombok.Data;
import jakarta.ws.rs.QueryParam;

@Data
public class AiMcpToolsQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}