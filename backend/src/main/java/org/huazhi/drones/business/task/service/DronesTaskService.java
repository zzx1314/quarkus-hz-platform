package org.huazhi.drones.business.task.service;

import java.util.List;

import org.huazhi.drones.business.task.entity.DronesTask;
import org.huazhi.drones.business.task.entity.dto.DronesTaskDto;
import org.huazhi.drones.business.task.entity.dto.DronesTaskQueryDto;
import org.huazhi.drones.business.task.entity.vo.DronesTaskStatusVo;
import org.huazhi.drones.business.task.entity.vo.DronesTaskVo;
import org.huazhi.drones.business.workflow.entity.DronesWorkflow;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface DronesTaskService {
   List<DronesTask> listEntitys();

   List<DronesTask> listEntitysByDto(DronesTaskQueryDto sysOrgDto);

   DronesTask listOne(DronesTaskQueryDto dto);

   PageResult<DronesTaskVo> listPage(DronesTaskQueryDto dto, PageRequest pageRequest);

   Long register(DronesTask entity);

   void replaceById(DronesTask entity);

   void replaceByDto(DronesTaskDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   List<Long> getTaskCount();

   List<Long> getTaskCountBefore();

   long count();

   void startTask(Long id);

   String getCommandJsonString(Long taskId);
   String getCommandJsonString(DronesWorkflow entity);

   DronesTaskStatusVo getTaskStatus(Long id);
}