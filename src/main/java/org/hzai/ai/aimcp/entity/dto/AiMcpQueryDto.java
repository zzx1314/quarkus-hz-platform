package org.hzai.ai.aimcp.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
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