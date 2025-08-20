package org.hzai.ai.aiapplication.service;

import java.util.List;

import org.hzai.ai.aiapplication.entity.AiApplication;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationDto;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiApplicationService {
   List<AiApplication> listEntitys();

   List<AiApplication> listEntitysByDto(AiApplicationQueryDto sysOrgDto);

   AiApplication listOne(AiApplicationQueryDto dto);

   PageResult<AiApplication> listPage(AiApplicationQueryDto dto, PageRequest pageRequest);

   Boolean register(AiApplication entity);

   replaceById(AiApplication entity);

   replaceByDto(AiApplicationDto dto);

   removeById(Long id);

   removeByIds(List<Long> ids);
}