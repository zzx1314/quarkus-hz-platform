package org.huazhi.ai.aimcptools.service;

import java.util.List;

import org.huazhi.ai.aimcptools.entity.AiMcpTools;
import org.huazhi.ai.aimcptools.entity.dto.AiMcpToolsDto;
import org.huazhi.ai.aimcptools.entity.dto.AiMcpToolsQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface AiMcpToolsService {
   List<AiMcpTools> listEntitys();

   List<AiMcpTools> listEntitysByDto(AiMcpToolsQueryDto sysOrgDto);

   AiMcpTools listOne(AiMcpToolsQueryDto dto);

   PageResult<AiMcpTools> listPage(AiMcpToolsQueryDto dto, PageRequest pageRequest);

   Boolean register(AiMcpTools entity);

   void replaceById(AiMcpTools entity);

   void replaceByDto(AiMcpToolsDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   Object findAllBySelectOption(Long id);
}