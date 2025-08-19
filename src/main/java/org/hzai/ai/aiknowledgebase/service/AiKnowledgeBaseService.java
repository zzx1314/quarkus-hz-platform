package org.hzai.ai.aiknowledgebase.service;

import java.util.List;

import org.hzai.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiKnowledgeBaseService {
   List<AiKnowledgeBase> listEntitys();

   List<AiKnowledgeBase> listEntitysByDto(AiKnowledgeBaseQueryDto sysOrgDto);

   PageResult<AiKnowledgeBase> listPage(AiKnowledgeBaseQueryDto dto, PageRequest pageRequest);

   Boolean register(AiKnowledgeBase entity);

   List<Long> getKnowledgeBaseCount();

   List<Long> getKnowledgeBaseCountBefore();

   long count();
}