package org.hzai.ai.aiapplication.entity.mapper;

import org.hzai.ai.aiapplication.entity.AiApplication;
import org.hzai.ai.aiapplication.entity.dto.AiApplicationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface AiApplicationMapper {
    AiApplication toEntity(AiApplicationDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiApplicationDto dto, @MappingTarget AiApplication entity);
}