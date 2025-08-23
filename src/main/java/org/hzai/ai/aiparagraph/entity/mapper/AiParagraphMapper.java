package org.hzai.ai.aiparagraph.entity.mapper;

import org.hzai.ai.aiparagraph.entity.AiParagraph;
import org.hzai.ai.aiparagraph.entity.dto.AiParagraphDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AiParagraphMapper {
    AiParagraph toEntity(AiParagraphDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiParagraphDto dto, @MappingTarget AiParagraph entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(AiParagraph dto, @MappingTarget AiParagraph entity);
}