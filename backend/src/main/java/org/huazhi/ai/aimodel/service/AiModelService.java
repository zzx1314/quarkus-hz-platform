package org.huazhi.ai.aimodel.service;

import java.util.List;

import org.huazhi.ai.aimodel.entity.AiModel;
import org.huazhi.ai.aimodel.entity.dto.AiModelDto;
import org.huazhi.ai.aimodel.entity.dto.AiModelQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;

public interface AiModelService {
   List<AiModel> listEntitys();

   List<AiModel> listEntitysByDto(AiModelQueryDto sysOrgDto);

   AiModel listOne(AiModelQueryDto dto);

   PageResult<AiModel> listPage(AiModelQueryDto dto, PageRequest pageRequest);

   Boolean register(AiModel entity);

   void replaceById(AiModel entity);

   void replaceByDto(AiModelDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   ChatModel getChatModel();

   StreamingChatModel getStreamingChatModel();

   Object findAllBySelectOption();
}