package org.huazhi.ai.aifinetuning.service;

import java.util.List;

import org.huazhi.ai.aifinetuning.entity.AiFineTuning;
import org.huazhi.ai.aifinetuning.entity.dto.AiFineTuningDto;
import org.huazhi.ai.aifinetuning.entity.dto.AiFineTuningQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface AiFineTuningService {
   List<AiFineTuning> listEntitys();

   List<AiFineTuning> listEntitysByDto(AiFineTuningQueryDto sysOrgDto);

   AiFineTuning listOne(AiFineTuningQueryDto dto);

   PageResult<AiFineTuning> listPage(AiFineTuningQueryDto dto, PageRequest pageRequest);

   Boolean register(AiFineTuning entity);

   void replaceById(AiFineTuning entity);

   void replaceByDto(AiFineTuningDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);
}