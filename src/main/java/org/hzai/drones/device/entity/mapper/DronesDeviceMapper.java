package org.hzai.drones.device.entity.mapper;

import org.hzai.drones.device.entity.DronesDevice;
import org.hzai.drones.device.entity.dto.DronesDeviceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesDeviceMapper {
    DronesDevice toEntity(DronesDeviceDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DronesDeviceDto dto, @MappingTarget DronesDevice entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(DronesDevice dto, @MappingTarget DronesDevice entity);
}