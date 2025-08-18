package org.hzai.ai.aimcp.entity.dto;

import lombok.Data;
import jakarta.ws.rs.QueryParam;

@Data
public class AiMcpQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}