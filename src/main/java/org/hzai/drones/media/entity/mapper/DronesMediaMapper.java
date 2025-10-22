package org.hzai.drones.media.entity.mapper;

import org.hzai.drones.media.entity.DronesMedia;
import org.hzai.drones.media.entity.dto.DronesMediaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesMediaMapper {
    DronesMedia toEntity(DronesMediaDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DronesMediaDto dto, @MappingTarget DronesMedia entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(DronesMedia dto, @MappingTarget DronesMedia entity);
}