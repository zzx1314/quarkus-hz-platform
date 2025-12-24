package org.huazhi.drones.business.task.entity.mapper;

import org.huazhi.drones.business.task.entity.DronesTask;
import org.huazhi.drones.business.task.entity.dto.DronesTaskDto;
import org.huazhi.drones.business.task.entity.vo.DronesTaskVo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesTaskMapper {
    DronesTask toEntity(DronesTaskDto dto);

    DronesTaskVo toVo(DronesTask entity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DronesTaskDto dto, @MappingTarget DronesTask entity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(DronesTask dto, @MappingTarget DronesTask entity);
}