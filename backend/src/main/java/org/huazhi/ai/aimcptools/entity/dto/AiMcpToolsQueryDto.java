package org.huazhi.ai.aimcptools.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
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