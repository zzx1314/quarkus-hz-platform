package org.hzai.drones.task.service;

import java.util.List;

import org.hzai.drones.task.entity.DronesTask;
import org.hzai.drones.task.entity.dto.DronesTaskDto;
import org.hzai.drones.task.entity.dto.DronesTaskQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

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
}