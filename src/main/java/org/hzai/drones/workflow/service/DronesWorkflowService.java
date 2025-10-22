package org.hzai.drones.workflow.service;

import java.util.List;

import org.hzai.drones.workflow.entity.DronesWorkflow;
import org.hzai.drones.workflow.entity.dto.DronesWorkflowDto;
import org.hzai.drones.workflow.entity.dto.DronesWorkflowQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface DronesWorkflowService {
   List<DronesWorkflow> listEntitys();

   List<DronesWorkflow> listEntitysByDto(DronesWorkflowQueryDto sysOrgDto);

   DronesWorkflow listOne(DronesWorkflowQueryDto dto);

   PageResult<DronesWorkflow> listPage(DronesWorkflowQueryDto dto, PageRequest pageRequest);

   Boolean register(DronesWorkflow entity);

   void replaceById(DronesWorkflow entity);

   void replaceByDto(DronesWorkflowDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);
}