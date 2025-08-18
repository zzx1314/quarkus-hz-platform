package org.hzai.ai.aiparagraph.service;

import java.util.List;

import org.hzai.ai.aiparagraph.entity.AiParagraph;
import org.hzai.ai.aiparagraph.entity.dto.AiParagraphQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface AiParagraphService {
   List<AiParagraph> listEntitys();

   List<AiParagraph> listEntitysByDto(AiParagraphQueryDto sysOrgDto);

   PageResult<AiParagraph> listPage(AiParagraphQueryDto dto, PageRequest pageRequest);

   Boolean register(AiParagraph entity);
}