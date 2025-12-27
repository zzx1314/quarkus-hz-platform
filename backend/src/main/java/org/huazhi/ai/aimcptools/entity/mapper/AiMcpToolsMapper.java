package org.huazhi.ai.aimcptools.entity.mapper;

import org.huazhi.ai.aimcptools.entity.AiMcpTools;
import org.huazhi.ai.aimcptools.entity.dto.AiMcpToolsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AiMcpToolsMapper {
    AiMcpTools toEntity(AiMcpToolsDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AiMcpToolsDto dto, @MappingTarget AiMcpTools entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(AiMcpTools dto, @MappingTarget AiMcpTools entity);
}