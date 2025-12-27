package org.huazhi.ai.aiprocess.service;

import java.util.List;

import org.huazhi.ai.aiprocess.entity.AiProcess;
import org.huazhi.ai.aiprocess.entity.dto.AiProcessDto;
import org.huazhi.ai.aiprocess.entity.dto.AiProcessQueryDto;
import org.huazhi.ai.aiprocess.entity.vo.AiProcessNet;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface AiProcessService {
   List<AiProcess> listEntitys();

   List<AiProcess> listEntitysByDto(AiProcessQueryDto sysOrgDto);

   AiProcess listOne(AiProcessQueryDto dto);

   PageResult<AiProcess> listPage(AiProcessQueryDto dto, PageRequest pageRequest);

   Boolean register(AiProcess entity);

   void replaceById(AiProcess entity);

   void replaceByDto(AiProcessDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   AiProcessNet getAiProcessNet(Long appId);

}