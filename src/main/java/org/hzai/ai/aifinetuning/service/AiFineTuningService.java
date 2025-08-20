package org.hzai.ai.aifinetuning.service;

import java.util.List;

import org.hzai.ai.aifinetuning.entity.AiFineTuning;
import org.hzai.ai.aifinetuning.entity.dto.AiFineTuningDto;
import org.hzai.ai.aifinetuning.entity.dto.AiFineTuningQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiFineTuningService {
   List<AiFineTuning> listEntitys();

   List<AiFineTuning> listEntitysByDto(AiFineTuningQueryDto sysOrgDto);

   AiFineTuning listOne(AiFineTuningQueryDto dto);

   PageResult<AiFineTuning> listPage(AiFineTuningQueryDto dto, PageRequest pageRequest);

   Boolean register(AiFineTuning entity);

   replaceById(AiFineTuning entity);

   replaceByDto(AiFineTuningDto dto);

   removeById(Long id);

   removeByIds(List<Long> ids);
}