package org.huazhi.ai.aiprocess.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
public class AiProcessQueryDto {
    @QueryParam("id")
    private Long id;

    private Long appId;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}