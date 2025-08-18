package org.hzai.ai.aifinetuning.service;

import java.util.List;

import org.hzai.ai.aifinetuning.entity.AiFineTuning;
import org.hzai.ai.aifinetuning.entity.dto.AiFineTuningQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiFineTuningService {
   List<AiFineTuning> listEntitys();

   List<AiFineTuning> listEntitysByDto(AiFineTuningQueryDto sysOrgDto);

   PageResult<AiFineTuning> listPage(AiFineTuningQueryDto dto, PageRequest pageRequest);

   Boolean register(AiFineTuning entity);
}