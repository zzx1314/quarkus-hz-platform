package org.hzai.ai.aiprocess.entity.mapper;

import org.hzai.ai.aiprocess.entity.AiProcess;
import org.hzai.ai.aiprocess.entity.dto.AiProcessDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface AiProcessMapper {
    AiProcess toEntity(AiProcessDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiProcessDto dto, @MappingTarget AiProcess entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(AiProcess dto, @MappingTarget AiProcess entity);
}