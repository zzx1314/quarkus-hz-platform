package org.hzai.ai.aiknowledgebase.entity.mapper;

import org.hzai.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.hzai.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface AiKnowledgeBaseMapper {
    AiKnowledgeBase toEntity(AiKnowledgeBaseDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiKnowledgeBaseDto dto, @MappingTarget AiKnowledgeBase entity);
}