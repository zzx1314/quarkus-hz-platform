package org.huazhi.ai.aiapplication.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import jakarta.ws.rs.QueryParam;

@Data
@Accessors(chain = true)
public class AiApplicationQueryDto {
    @QueryParam("id")
    private Long id;

    @QueryParam("beginTime")
    private String beginTime;

    @QueryParam("endTime")
    private String endTime;

    private Long createId;

    private List<Long> roleIdList;
}