package org.hzai.ai.aidocument.service;

import java.util.List;

import org.hzai.ai.aidocument.entity.AiDocument;
import org.hzai.ai.aidocument.entity.dto.AiDocumentQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiDocumentService {
   List<AiDocument> listEntitys();

   List<AiDocument> listEntitysByDto(AiDocumentQueryDto sysOrgDto);

   PageResult<AiDocument> listPage(AiDocumentQueryDto dto, PageRequest pageRequest);

   Boolean register(AiDocument entity);

   List<Long> getDocumentCount();

   long count();
}