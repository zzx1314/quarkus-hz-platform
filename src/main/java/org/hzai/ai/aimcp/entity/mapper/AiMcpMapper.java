package org.hzai.ai.aimcp.entity.mapper;

import org.hzai.ai.aimcp.entity.AiMcp;
import org.hzai.ai.aimcp.entity.dto.AiMcpDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AiMcpMapper {
    AiMcp toEntity(AiMcpDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiMcpDto dto, @MappingTarget AiMcp entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(AiMcp dto, @MappingTarget AiMcp entity);
}