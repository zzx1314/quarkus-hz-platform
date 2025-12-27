package org.huazhi.ai.aiknowledgebase.entity.mapper;

import org.huazhi.ai.aiknowledgebase.entity.AiKnowledgeBase;
import org.huazhi.ai.aiknowledgebase.entity.dto.AiKnowledgeBaseDto;
import org.huazhi.ai.aiknowledgebase.entity.vo.AiKnowledgeBaseVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AiKnowledgeBaseMapper {
    AiKnowledgeBase toEntity(AiKnowledgeBaseDto dto);

    AiKnowledgeBaseVo toVo(AiKnowledgeBase entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiKnowledgeBaseDto dto, @MappingTarget AiKnowledgeBase entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(AiKnowledgeBase dto, @MappingTarget AiKnowledgeBase entity);
}