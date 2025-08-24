package org.hzai.ai.aimodel.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
public class AiModelQueryDto {
    @QueryParam("id")
    private Long id;

    private String enable;

    private String modelType;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;
}