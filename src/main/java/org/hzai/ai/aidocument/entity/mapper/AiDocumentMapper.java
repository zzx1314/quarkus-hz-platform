package org.hzai.ai.aidocument.entity.mapper;

import org.hzai.ai.aidocument.entity.AiDocument;
import org.hzai.ai.aidocument.entity.dto.AiDocumentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface AiDocumentMapper {
    AiDocument toEntity(AiDocumentDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiDocumentDto dto, @MappingTarget AiDocument entity);
}