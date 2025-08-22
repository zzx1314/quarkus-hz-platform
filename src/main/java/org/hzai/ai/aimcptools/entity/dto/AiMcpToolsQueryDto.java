package org.hzai.ai.aimcptools.entity.dto;

import lombok.Data;
import jakarta.ws.rs.QueryParam;

@Data
public class AiMcpToolsQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("mcpId")
    private Long mcpId;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}