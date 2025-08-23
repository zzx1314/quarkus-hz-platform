package org.hzai.ai.aiapplication.entity.dto;

import lombok.Data;

import java.util.List;

import jakarta.ws.rs.QueryParam;

@Data
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