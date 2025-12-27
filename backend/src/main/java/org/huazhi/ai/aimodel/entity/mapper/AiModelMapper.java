package org.huazhi.ai.aimodel.entity.mapper;

import org.huazhi.ai.aimodel.entity.AiModel;
import org.huazhi.ai.aimodel.entity.dto.AiModelDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AiModelMapper {
    AiModel toEntity(AiModelDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiModelDto dto, @MappingTarget AiModel entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(AiModel dto, @MappingTarget AiModel entity);
}