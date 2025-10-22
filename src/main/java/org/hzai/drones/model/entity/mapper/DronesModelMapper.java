package org.hzai.drones.model.entity.mapper;

import org.hzai.drones.model.entity.DronesModel;
import org.hzai.drones.model.entity.dto.DronesModelDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesModelMapper {
    DronesModel toEntity(DronesModelDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DronesModelDto dto, @MappingTarget DronesModel entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(DronesModel dto, @MappingTarget DronesModel entity);
}