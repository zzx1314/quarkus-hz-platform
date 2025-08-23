package org.hzai.ai.aimcp.entity.dto;

import lombok.Data;
import jakarta.ws.rs.QueryParam;

@Data
public class AiMcpQueryDto {
    @QueryParam("id")
    private Long id;

    private String name;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    @QueryParam("enable")
    private String enable;
}