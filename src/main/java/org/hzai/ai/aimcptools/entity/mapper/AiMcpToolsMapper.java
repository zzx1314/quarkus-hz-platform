package org.hzai.ai.aimcptools.entity.mapper;

import org.hzai.ai.aimcptools.entity.AiMcpTools;
import org.hzai.ai.aimcptools.entity.dto.AiMcpToolsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface AiMcpToolsMapper {
    AiMcpTools toEntity(AiMcpToolsDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiMcpToolsDto dto, @MappingTarget AiMcpTools entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(AiMcpTools dto, @MappingTarget AiMcpTools entity);
}