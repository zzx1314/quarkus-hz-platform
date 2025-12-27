package org.huazhi.ai.aiprocess.entity.mapper;

import org.huazhi.ai.aiprocess.entity.AiProcess;
import org.huazhi.ai.aiprocess.entity.dto.AiProcessDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AiProcessMapper {
    AiProcess toEntity(AiProcessDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiProcessDto dto, @MappingTarget AiProcess entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(AiProcess dto, @MappingTarget AiProcess entity);
}