package org.hzai.ai.aimcptools.service;

import java.util.List;

import org.hzai.ai.aimcptools.entity.AiMcpTools;
import org.hzai.ai.aimcptools.entity.dto.AiMcpToolsDto;
import org.hzai.ai.aimcptools.entity.dto.AiMcpToolsQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiMcpToolsService {
   List<AiMcpTools> listEntitys();

   List<AiMcpTools> listEntitysByDto(AiMcpToolsQueryDto sysOrgDto);

   AiMcpTools listOne(AiMcpToolsQueryDto dto);

   PageResult<AiMcpTools> listPage(AiMcpToolsQueryDto dto, PageRequest pageRequest);

   Boolean register(AiMcpTools entity);

   replaceById(AiMcpTools entity);

   replaceByDto(AiMcpToolsDto dto);

   removeById(Long id);

   removeByIds(List<Long> ids);
}