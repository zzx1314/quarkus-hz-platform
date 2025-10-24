package org.hzai.drones.command.service;

import java.util.List;

import org.hzai.drones.command.entity.DronesCommand;
import org.hzai.drones.command.entity.dto.DronesCommandDto;
import org.hzai.drones.command.entity.dto.DronesCommandQueryDto;
import org.hzai.util.PageRequest;
import org.hzai.util.PageResult;

public interface DronesCommandService {
   List<DronesCommand> listEntitys();

   List<DronesCommand> listEntitysByDto(DronesCommandQueryDto sysOrgDto);

   DronesCommand listOne(DronesCommandQueryDto dto);

   PageResult<DronesCommand> listPage(DronesCommandQueryDto dto, PageRequest pageRequest);

   Boolean register(DronesCommand entity);

   void replaceById(DronesCommand entity);

   void replaceByDto(DronesCommandDto dto);

   void removeById(Long id);

   void removeByIds(List<Long> ids);
}