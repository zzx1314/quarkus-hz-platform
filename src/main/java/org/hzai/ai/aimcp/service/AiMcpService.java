package org.hzai.ai.aimcp.service;

import java.util.List;

import org.hzai.ai.aimcp.entity.AiMcp;
import org.hzai.ai.aimcp.entity.dto.AiMcpQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiMcpService {
   List<AiMcp> listEntitys();

   List<AiMcp> listEntitysByDto(AiMcpQueryDto sysOrgDto);

   PageResult<AiMcp> listPage(AiMcpQueryDto dto, PageRequest pageRequest);

   Boolean register(AiMcp entity);
}