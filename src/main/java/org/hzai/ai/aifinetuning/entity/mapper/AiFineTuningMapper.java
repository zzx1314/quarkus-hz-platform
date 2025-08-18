package org.hzai.ai.aifinetuning.entity.mapper;

import org.hzai.ai.aifinetuning.entity.AiFineTuning;
import org.hzai.ai.aifinetuning.entity.dto.AiFineTuningDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface AiFineTuningMapper {
    AiFineTuning toEntity(AiFineTuningDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiFineTuningDto dto, @MappingTarget AiFineTuning entity);
}