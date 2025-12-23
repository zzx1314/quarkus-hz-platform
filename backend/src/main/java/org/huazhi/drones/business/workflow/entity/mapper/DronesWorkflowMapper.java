package org.huazhi.drones.business.workflow.entity.mapper;

import org.huazhi.drones.business.workflow.entity.DronesWorkflow;
import org.huazhi.drones.business.workflow.entity.dto.DronesWorkflowDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesWorkflowMapper {
    DronesWorkflow toEntity(DronesWorkflowDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DronesWorkflowDto dto, @MappingTarget DronesWorkflow entity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(DronesWorkflow dto, @MappingTarget DronesWorkflow entity);
}