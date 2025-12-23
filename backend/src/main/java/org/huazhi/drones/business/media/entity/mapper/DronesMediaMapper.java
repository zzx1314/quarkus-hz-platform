package org.huazhi.drones.business.media.entity.mapper;

import org.huazhi.drones.business.media.entity.DronesMedia;
import org.huazhi.drones.business.media.entity.dto.DronesMediaDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesMediaMapper {
    DronesMedia toEntity(DronesMediaDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DronesMediaDto dto, @MappingTarget DronesMedia entity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(DronesMedia dto, @MappingTarget DronesMedia entity);
}