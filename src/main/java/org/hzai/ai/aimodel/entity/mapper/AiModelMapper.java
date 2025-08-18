package org.hzai.ai.aimodel.entity.mapper;

import org.hzai.ai.aimodel.entity.AiModel;
import org.hzai.ai.aimodel.entity.dto.AiModelDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface AiModelMapper {
    AiModel toEntity(AiModelDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiModelDto dto, @MappingTarget AiModel entity);
}