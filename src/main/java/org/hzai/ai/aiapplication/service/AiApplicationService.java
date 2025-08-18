package org.hzai.ai.aiapplication.service;

import java.util.List;

import org.hzai.ai.aiapplication.entity.AiApplication;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiApplicationService {
   List<AiApplication> listEntitys();

   List<AiApplication> listEntitysByDto(AiApplicationQueryDto sysOrgDto);

   PageResult<AiApplication> listPage(AiApplicationQueryDto dto, PageRequest pageRequest);

   Boolean register(AiApplication entity);
}