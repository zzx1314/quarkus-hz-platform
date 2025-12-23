package org.huazhi.drones.business.routelibrary.routeitem.entity.mapper;

import org.huazhi.drones.business.routelibrary.routeitem.entity.DronesRouteItem;
import org.huazhi.drones.business.routelibrary.routeitem.entity.dto.DronesRouteItemDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesRouteItemMapper {
    DronesRouteItem toEntity(DronesRouteItemDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DronesRouteItemDto dto, @MappingTarget DronesRouteItem entity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(DronesRouteItem dto, @MappingTarget DronesRouteItem entity);
}