package org.hzai.ai.aiknowledgebase.service;

import java.util.List;

import org.hzai.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseDto;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiKnowledgeBaseService {
   List<AiKnowledgeBase> listEntitys();

   List<AiKnowledgeBase> listEntitysByDto(AiKnowledgeBaseQueryDto sysOrgDto);

   AiKnowledgeBase listOne(AiKnowledgeBaseQueryDto dto);

   PageResult<AiKnowledgeBase> listPage(AiKnowledgeBaseQueryDto dto, PageRequest pageRequest);

   Boolean register(AiKnowledgeBase entity);

   replaceById(AiKnowledgeBase entity);

   replaceByDto(AiKnowledgeBaseDto dto);

   removeById(Long id);

   removeByIds(List<Long> ids);
}