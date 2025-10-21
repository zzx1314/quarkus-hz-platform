package org.hzai.drones.routelibrary.entity.mapper;

import org.hzai.drones.routelibrary.entity.DronesRouteLibrary;
import org.hzai.drones.routelibrary.entity.dto.DronesRouteLibraryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesRouteLibraryMapper {
    DronesRouteLibrary toEntity(DronesRouteLibraryDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DronesRouteLibraryDto dto, @MappingTarget DronesRouteLibrary entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(DronesRouteLibrary dto, @MappingTarget DronesRouteLibrary entity);
}