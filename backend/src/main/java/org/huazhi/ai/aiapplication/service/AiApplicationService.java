package org.huazhi.ai.aiapplication.service;

import java.util.List;

import org.huazhi.ai.aiapplication.entity.AiApplication;
import org.huazhi.ai.aiapplication.entity.dto.AiApplicationDto;
import org.huazhi.ai.aiapplication.entity.dto.AiApplicationQueryDto;
import org.huazhi.ai.aiapplication.entity.vo.AiApplicationVo;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import io.smallrye.mutiny.Multi;

public interface AiApplicationService {
   List<AiApplication> listEntitys();

   List<AiApplication> listEntitysByDto(AiApplicationQueryDto sysOrgDto);

   PageResult<AiApplicationVo> listPage(AiApplicationQueryDto dto, PageRequest pageRequest);

   Boolean register(AiApplicationDto entity);

   List<Long> getApplicationCount();

   List<Long> getApplicationCountBefore();

   long count();

   Multi<String> chat(Long appId, String question, String filepath);

   void replaceById(AiApplication aiApplication);

   void replaceData(AiApplicationDto aiApplication);
}