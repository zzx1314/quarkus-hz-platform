package org.huazhi.drones.routelibrary.entity.mapper;

import org.huazhi.drones.routelibrary.entity.DronesRouteLibrary;
import org.huazhi.drones.routelibrary.entity.dto.DronesRouteLibraryDto;
import org.huazhi.drones.routelibrary.entity.vo.DronesRouteLibraryVo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesRouteLibraryMapper {
    DronesRouteLibrary toEntity(DronesRouteLibraryDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DronesRouteLibraryDto dto, @MappingTarget DronesRouteLibrary entity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(DronesRouteLibrary dto, @MappingTarget DronesRouteLibrary entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toVo(DronesRouteLibrary entity, @MappingTarget DronesRouteLibraryVo vo);
}