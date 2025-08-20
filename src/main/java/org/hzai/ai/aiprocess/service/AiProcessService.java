package org.hzai.ai.aiprocess.service;

import java.util.List;

import org.hzai.ai.aiprocess.entity.AiProcess;
import org.hzai.ai.aiprocess.entity.dto.AiProcessDto;
import org.hzai.ai.aiprocess.entity.dto.AiProcessQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiProcessService {
   List<AiProcess> listEntitys();

   List<AiProcess> listEntitysByDto(AiProcessQueryDto sysOrgDto);

   AiProcess listOne(AiProcessQueryDto dto);

   PageResult<AiProcess> listPage(AiProcessQueryDto dto, PageRequest pageRequest);

   Boolean register(AiProcess entity);

   replaceById(AiProcess entity);

   replaceByDto(AiProcessDto dto);

   removeById(Long id);

   removeByIds(List<Long> ids);
}