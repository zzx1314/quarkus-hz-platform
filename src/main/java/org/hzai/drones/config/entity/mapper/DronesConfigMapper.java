package org.hzai.drones.config.entity.mapper;

import org.hzai.drones.config.entity.DronesConfig;
import org.hzai.drones.config.entity.dto.DronesConfigDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesConfigMapper {
    DronesConfig toEntity(DronesConfigDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DronesConfigDto dto, @MappingTarget DronesConfig entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(DronesConfig dto, @MappingTarget DronesConfig entity);
}