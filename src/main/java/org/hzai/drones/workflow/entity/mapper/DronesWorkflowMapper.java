package org.hzai.drones.workflow.entity.mapper;

import org.hzai.drones.workflow.entity.DronesWorkflow;
import org.hzai.drones.workflow.entity.dto.DronesWorkflowDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "cdi" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DronesWorkflowMapper {
    DronesWorkflow toEntity(DronesWorkflowDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DronesWorkflowDto dto, @MappingTarget DronesWorkflow entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(DronesWorkflow dto, @MappingTarget DronesWorkflow entity);
}