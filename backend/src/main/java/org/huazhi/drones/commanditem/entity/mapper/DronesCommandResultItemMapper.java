package org.huazhi.drones.commanditem.entity.mapper;

import org.huazhi.drones.commanditem.entity.DronesCommandResultItem;
import org.huazhi.drones.commanditem.entity.dto.DronesCommandResultItemDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesCommandResultItemMapper {
    DronesCommandResultItem toEntity(DronesCommandResultItemDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DronesCommandResultItemDto dto, @MappingTarget DronesCommandResultItem entity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(DronesCommandResultItem dto, @MappingTarget DronesCommandResultItem entity);
}