package org.hzai.ai.aiapplication.service;

import java.util.List;

import org.hzai.ai.aiapplication.entity.AiApplication;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationDto;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

import io.smallrye.mutiny.Multi;

public interface AiApplicationService {
   List<AiApplication> listEntitys();

   List<AiApplication> listEntitysByDto(AiApplicationQueryDto sysOrgDto);

   PageResult<AiApplication> listPage(AiApplicationQueryDto dto, PageRequest pageRequest);

   Boolean register(AiApplicationDto entity);

   List<Long> getApplicationCount();

   List<Long> getApplicationCountBefore();

   long count();

   Multi<String> chat(Long appId, String question, String filepath);

   void replaceById(AiApplication aiApplication);

   void replaceData(AiApplicationDto aiApplication);
}