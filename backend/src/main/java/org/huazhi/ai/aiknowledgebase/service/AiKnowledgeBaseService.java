package org.huazhi.ai.aiknowledgebase.service;

import java.util.List;

import org.huazhi.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.huazhi.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseDto;
import org.huazhi.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseQueryDto;
import org.huazhi.ai.aiknowledgebase.entity.vo.AiKnowledgeBaseVo;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

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

   List<AiKnowledgeBase> listByIds(List<Long> knowledgeIds);
}