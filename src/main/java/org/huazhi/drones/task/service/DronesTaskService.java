package org.huazhi.drones.task.service;

import java.util.List;

import org.huazhi.drones.task.entity.DronesTask;
import org.huazhi.drones.task.entity.dto.DronesTaskDto;
import org.huazhi.drones.task.entity.dto.DronesTaskQueryDto;
import org.huazhi.util.PageRequest;
import org.huazhi.util.PageResult;

public interface DronesTaskService {
   List<DronesTask> listEntitys();

   List<DronesTask> listEntitysByDto(DronesTaskQueryDto sysOrgDto);

   DronesTask listOne(DronesTaskQueryDto dto);

   PageResult<DronesTask> listPage(DronesTaskQueryDto dto, PageRequest pageRequest);

   Boolean register(DronesTask entity);

   void replaceById(DronesTask entity);

   void replaceByDto(DronesTaskDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);

   List<Long> getTaskCount();

   List<Long> getTaskCountBefore();

   long count();

   void startTask(Long id);
}