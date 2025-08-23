package org.hzai.ai.aifinetuning.entity.mapper;

import org.hzai.ai.aifinetuning.entity.AiFineTuning;
import org.hzai.ai.aifinetuning.entity.dto.AiFineTuningDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AiFineTuningMapper {
    AiFineTuning toEntity(AiFineTuningDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiFineTuningDto dto, @MappingTarget AiFineTuning entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(AiFineTuning dto, @MappingTarget AiFineTuning entity);
}