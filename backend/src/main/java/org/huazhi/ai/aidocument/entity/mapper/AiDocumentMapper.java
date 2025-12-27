package org.huazhi.ai.aidocument.entity.mapper;

import org.huazhi.ai.aidocument.entity.AiDocument;
import org.huazhi.ai.aidocument.entity.dto.AiDocumentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AiDocumentMapper {
    AiDocument toEntity(AiDocumentDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiDocumentDto dto, @MappingTarget AiDocument entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(AiDocument dto, @MappingTarget AiDocument entity);
}