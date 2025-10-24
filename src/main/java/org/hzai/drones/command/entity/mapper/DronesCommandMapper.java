package org.hzai.drones.command.entity.mapper;

import org.hzai.drones.command.entity.DronesCommand;
import org.hzai.drones.command.entity.dto.DronesCommandDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesCommandMapper {
    DronesCommand toEntity(DronesCommandDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DronesCommandDto dto, @MappingTarget DronesCommand entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(DronesCommand dto, @MappingTarget DronesCommand entity);
}