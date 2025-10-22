package org.hzai.drones.task.entity.mapper;

import org.hzai.drones.task.entity.DronesTask;
import org.hzai.drones.task.entity.dto.DronesTaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesTaskMapper {
    DronesTask toEntity(DronesTaskDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DronesTaskDto dto, @MappingTarget DronesTask entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(DronesTask dto, @MappingTarget DronesTask entity);
}