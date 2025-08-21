package org.hzai.ai.aiknowledgebase.service;

import java.util.List;

import org.hzai.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseDto;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseQueryDto;
import org.hzai.ai.aiknowledgebase.entity.vo.AiKnowledgeBaseVo;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiKnowledgeBaseService {
   List<AiKnowledgeBase> listEntitys();

   List<AiKnowledgeBase> listEntitysByDto(AiKnowledgeBaseQueryDto sysOrgDto);

   AiKnowledgeBase listOne(AiKnowledgeBaseQueryDto dto);

   PageResult<AiKnowledgeBaseVo> listPage(AiKnowledgeBaseQueryDto dto, PageRequest pageRequest);

   Boolean register(AiKnowledgeBase entity);

   void replaceById(AiKnowledgeBase entity);

   void replaceByDto(AiKnowledgeBaseDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   List<Long> getKnowledgeBaseCount();

   List<Long> getKnowledgeBaseCountBefore();

   long count();
}