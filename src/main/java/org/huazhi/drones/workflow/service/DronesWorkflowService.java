package org.huazhi.drones.workflow.service;

import java.util.List;

import org.huazhi.drones.routelibrary.entity.vo.DronesRouteLibraryVo;
import org.huazhi.drones.workflow.entity.DronesWorkflow;
import org.huazhi.drones.workflow.entity.dto.DronesWorkflowDto;
import org.huazhi.drones.workflow.entity.dto.DronesWorkflowQueryDto;
import org.huazhi.drones.workflow.vo.DronesWorkflowVo;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface DronesWorkflowService {
   List<DronesWorkflow> listEntitys();

   List<DronesWorkflow> listEntitysByDto(DronesWorkflowQueryDto sysOrgDto);

   DronesWorkflow listOne(DronesWorkflowQueryDto dto);

   PageResult<DronesWorkflow> listPage(DronesWorkflowQueryDto dto, PageRequest pageRequest);

   Boolean register(DronesWorkflow entity);

   void replaceById(DronesWorkflow entity);

   void replaceByDto(DronesWorkflowQueryDto queryDto, DronesWorkflowDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   DronesWorkflowVo getWorkflowGraph(Long taskId);

   DronesWorkflow getWorkflow(Long taskId);

   List<DronesRouteLibraryVo> getRouteByTaskId(Long taskId);
}