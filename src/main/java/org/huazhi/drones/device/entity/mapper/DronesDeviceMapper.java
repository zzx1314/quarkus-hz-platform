package org.huazhi.drones.device.entity.mapper;

import org.huazhi.drones.device.entity.DronesDevice;
import org.huazhi.drones.device.entity.dto.DronesDeviceDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesDeviceMapper {
    DronesDevice toEntity(DronesDeviceDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DronesDeviceDto dto, @MappingTarget DronesDevice entity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(DronesDevice dto, @MappingTarget DronesDevice entity);
}