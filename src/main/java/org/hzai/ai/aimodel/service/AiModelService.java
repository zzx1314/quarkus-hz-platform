package org.hzai.ai.aimodel.service;

import java.util.List;

import org.hzai.ai.aimodel.entity.AiModel;
import org.hzai.ai.aimodel.entity.dto.AiModelDto;
import org.hzai.ai.aimodel.entity.dto.AiModelQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiModelService {
   List<AiModel> listEntitys();

   List<AiModel> listEntitysByDto(AiModelQueryDto sysOrgDto);

   AiModel listOne(AiModelQueryDto dto);

   PageResult<AiModel> listPage(AiModelQueryDto dto, PageRequest pageRequest);

   Boolean register(AiModel entity);

   replaceById(AiModel entity);

   replaceByDto(AiModelDto dto);

   removeById(Long id);

   removeByIds(List<Long> ids);
}