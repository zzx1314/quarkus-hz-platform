package org.huazhi.ai.aiapplication.entity.mapper;

import org.huazhi.ai.aiapplication.entity.AiApplication;
import org.huazhi.ai.aiapplication.entity.dto.AiApplicationDto;
import org.huazhi.ai.aiapplication.entity.vo.AiApplicationVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AiApplicationMapper {
    AiApplication toEntity(AiApplicationDto dto);

    AiApplicationVo toVo(AiApplication entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiApplicationDto dto, @MappingTarget AiApplication entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(AiApplication dto, @MappingTarget AiApplication entity);
}