package org.huazhi.ai.aiparagraph.service;

import java.util.List;

import org.huazhi.ai.aiparagraph.entity.AiParagraph;
import org.huazhi.ai.aiparagraph.entity.dto.AiParagraphDto;
import org.huazhi.ai.aiparagraph.entity.dto.AiParagraphQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface AiParagraphService {
   List<AiParagraph> listEntitys();

   List<AiParagraph> listEntitysByDto(AiParagraphQueryDto sysOrgDto);

   AiParagraph listOne(AiParagraphQueryDto sAiParagraphQueryDto);

   PageResult<AiParagraph> listPage(AiParagraphQueryDto dto, PageRequest pageRequest);

   Boolean register(AiParagraph entity);

   List<Long> getApplicationCountBefore();

   AiParagraph listById(Long id);

   void removeByDocumentId(Long documentId);

   void removeById(Long id);


   void replaceById(AiParagraph aiParagraph);

   
   void replaceByDto(AiParagraphDto dto);


}