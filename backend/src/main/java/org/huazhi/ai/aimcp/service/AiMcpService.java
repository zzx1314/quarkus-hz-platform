package org.huazhi.ai.aimcp.service;

import java.util.List;

import org.huazhi.ai.aimcp.entity.AiMcp;
import org.huazhi.ai.aimcp.entity.dto.AiMcpDto;
import org.huazhi.ai.aimcp.entity.dto.AiMcpQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;
import org.huazhi.util.R;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import dev.langchain4j.mcp.client.McpClient;

public interface AiMcpService {
   List<AiMcp> listEntitys();

   List<AiMcp> listEntitysByDto(AiMcpQueryDto sysOrgDto);

   PageResult<AiMcp> listPage(AiMcpQueryDto dto, PageRequest pageRequest);

   Boolean register(AiMcp entity);

   List<Long> getMcpCount();

   List<Long> getMcpCountBefore();

   long count();

   void replaceByDto(AiMcpDto dto);


   void replaceById(AiMcp entity);

   R<Void> uploadFile(FileUpload file, AiMcp aiMcp) throws Exception;

   McpClient getMcpClientById(AiMcp aiMcp);

   Object findAllBySelectOption();

   String callMcpTools(Long mcpId, Long mcpToolId, String arguments, String question);

   AiMcp listEntityById(Long oneId);
}